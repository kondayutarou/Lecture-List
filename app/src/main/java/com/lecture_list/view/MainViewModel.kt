package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepositoryInterface
import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiRepositoryInterface
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.model.ApiServerError
import com.lecture_list.model.LectureListApiItem
import com.lecture_list.model.LectureListItem
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.blockingSubscribeBy
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val db: AppDatabase,
    private val lectureListApiRepository: LectureListApiRepositoryInterface,
    private val lectureProgressApiRepository: LectureProgressApiRepositoryInterface
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val lectureListForProgressApi = PublishRelay.create<List<LectureListApiItem>>()
    val lectureListForView = BehaviorRelay.create<List<LectureListItem>>()

    val errorRelay = PublishRelay.create<ApiServerError>()

    fun start() {
        loadApi()
        setupRx()
    }

    fun loadApi() {
        lectureListApiRepository.fetchLectureListObservable().share()
            .subscribeBy(onNext = { list ->
                lectureListForProgressApi.accept(list)
                Logger.d(list.map { it.id })
            }, onError = {
                val error = it as? ApiServerError ?: return@subscribeBy
                errorRelay.accept(error)
                Logger.d(error)
            })
            .addTo(compositeDisposable)
    }

    private fun setupRx() {
        val newList = mutableListOf<LectureListItem>()
        lectureListForProgressApi.observeOn(Schedulers.io())
            .flatMap {
                val oldList = it
                val observableList = oldList.map { lectureListItem ->
                    Pair(
                        lectureProgressApiRepository.fetchLectureListObservable(lectureListItem.id),
                        lectureListItem
                    )
                }
                observableList.forEach { pair ->
                    pair.first.blockingSubscribeBy(onNext = { apiItem ->
                        val newItem = LectureListItem.fromListApi(pair.second)
                        newItem.progress = apiItem.progress
                        newList.add(newItem)
                    }, onError = { throwable ->
                        Logger.d(throwable)
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

    fun finish() {
        compositeDisposable.clear()
    }
}