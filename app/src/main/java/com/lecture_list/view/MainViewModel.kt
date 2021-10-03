package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.lecture_list.data.LectureListDB
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepositoryInterface
import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiRepositoryInterface
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.model.*
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.blockingSubscribeBy
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.zipWith
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val db: AppDatabase,
    private val lectureListApiRepository: LectureListApiRepositoryInterface,
    private val lectureProgressApiRepository: LectureProgressApiRepositoryInterface
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val lectureListForProgressApi =
        PublishRelay.create<Pair<List<LectureListApiItem>, List<LectureListDB>?>>()
    val lectureListForView = BehaviorRelay.create<List<LectureListItem>>()

    val serverErrorRelay = PublishRelay.create<ApiServerError>()
    val networkErrorRelay = PublishRelay.create<ApiNetworkingError>()

    fun start() {
        loadApi()
        setupRx()
    }

    fun loadApi() {
        lectureListApiRepository.fetchLectureListObservable()
            .zipWith(loadData())
            .subscribeBy(onSuccess = { pair ->
                // Retrieve stored lectures if available
                if (pair.second.isNotEmpty()) {
                    lectureListForProgressApi.accept(Pair(pair.first, pair.second))
                    Logger.d("success")
                } else {
                    lectureListForProgressApi.accept(Pair(pair.first, null))
                    Logger.d(pair.first.map { it.id })
                }
            }, onError = {
                val serverError = it as? ApiServerError
                val networkingError = it as? ApiNetworkingError
                serverError?.let { error -> serverErrorRelay.accept(error) }
                networkingError?.let { error -> networkErrorRelay.accept(error) }
            })
            .addTo(compositeDisposable)
    }

    private fun setupRx() {
        val newList = mutableListOf<LectureListItem>()
        lectureListForProgressApi.observeOn(Schedulers.io())
            .flatMap {
                val oldList = mergeApiWithDBData(it)
                val observableList = oldList.map { lectureListItem ->
                    Pair(
                        lectureProgressApiRepository.fetchLectureListObservable(lectureListItem.id),
                        lectureListItem
                    )
                }
                observableList.forEach { pair ->
                    pair.first.blockingSubscribeBy(onSuccess = { apiItem ->
                        progressApiOnSuccess(newList, pair, apiItem)
                    }, onError = { throwable ->
                        val pairId = pair.second.id
                        newList.find { it.id == pairId }?.progressError = true
                    })
                }
                Observable.just(newList)
            }
            .subscribeBy(onNext = {
                Logger.d(it)
                lectureListForView.accept(it)
            }, onError = {
                Logger.d(it)
            })
            .addTo(compositeDisposable)
    }

    private fun progressApiOnSuccess(
        newList: MutableList<LectureListItem>,
        observableListItem: Pair<Single<LectureProgressApiItem>, LectureListItem>,
        apiItem: LectureProgressApiItem
    ) {
        val matchIndex = newList.indexOfFirst { it.id == apiItem.id }
        if (matchIndex == -1) {
            // At the time of first api call
            val newItem = observableListItem.second
            newItem.progress = apiItem.progress
            newItem.progressError = false
            newList.add(newItem)
        } else {
            // From second api call onwards, replace items with identical id
            newList[matchIndex].apply {
                progress = apiItem.progress
                progressError = false
            }
            Logger.d(newList[matchIndex].progress)
        }
    }

    private fun mergeApiWithDBData(dataPair: Pair<List<LectureListApiItem>, List<LectureListDB>?>): List<LectureListItem> {
        val newList = (dataPair.first.map { LectureListItem.fromListApi(it) }).toMutableList()

        // If Database data is empty, return apiItemList
        val dbList =
            dataPair.second ?: return newList

        val convertedDbList = dbList.map { it.toLectureListItem() }
        newList.mapIndexed { index, item ->
            val dbListIndex =
                convertedDbList.indexOfFirst { it.id == item.id }
            if (dbListIndex != -1) {
                val bookmark = convertedDbList[dbListIndex].bookmarked
                item.bookmarked = bookmark
            }
        }

        return newList
    }

    fun saveData() {
        val currentLectureList =
            lectureListForView.value?.map { return@map it.toDBClass() } ?: return

        db.lectureListItemDao()
            .deleteAll()
            .subscribeOn(Schedulers.io())
            .andThen {
                db.lectureListItemDao().insertAll(currentLectureList)
                    .doOnSubscribe { Logger.d("Save finished subscribed") }
                    .subscribe {
                        Logger.d("Save finished")
                    }
                    .addTo(compositeDisposable)
            }
            .subscribe {
                Logger.d("delete finished")
            }
            .addTo(compositeDisposable)

    }

    fun loadData(): @NonNull Single<List<LectureListDB>> {
        return db.lectureListItemDao().getAll().subscribeOn(Schedulers.io())
            .doOnSubscribe { Logger.d("list loaded subscribed") }
    }

    fun changeBookmarkState(bookmark: Boolean, item: LectureListItem) {
        item.bookmarked = bookmark
        db.lectureListItemDao().update(item.toDBClass()).subscribeOn(Schedulers.io())
            .subscribe {
                Logger.d("Bookmark stored")
            }
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}