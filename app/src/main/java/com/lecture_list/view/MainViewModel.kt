package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepositoryInterface
import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiRepositoryInterface
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.model.LectureListItem
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val db: AppDatabase,
    private val lectureListApiRepository: LectureListApiRepositoryInterface,
    private val lectureProgressApiRepository: LectureProgressApiRepositoryInterface
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val lectureListItem = BehaviorRelay.create<List<LectureListItem>>()

    // Notify the view of its data change
    val shouldUpdateView = PublishRelay.create<Unit>()

    fun start() {
        loadApi()
    }

    fun loadApi() {
        val sharedLectureList = lectureListApiRepository.fetchLectureListObservable().share()

        sharedLectureList
            .subscribeBy(onNext = { list ->
                lectureListItem.accept(list)
                shouldUpdateView.accept(Unit)
            }, onError = {
                Logger.d(it.localizedMessage)
            })
            .addTo(compositeDisposable)

        sharedLectureList.map { list -> list.map { it.id } }
            .subscribeBy(onNext = { list ->
                list.forEach { loadProgress(it) }
            })
            .addTo(compositeDisposable)
    }

    fun loadProgress(courseId: String) {
        lectureProgressApiRepository.fetchLectureListObservable(courseId)
            .subscribeBy(onNext = { apiItem ->
                val list = lectureListItem.value?.toMutableList() ?: return@subscribeBy
                list.map { item ->
                    if (item.id == apiItem.id) {
                        item.updateProgress(apiItem.progress)
                    }
                }
                lectureListItem.accept(list)
            }, onError = {
                Logger.d(it.localizedMessage)
            })
            .addTo(compositeDisposable)
    }

    fun finish() {
        compositeDisposable.clear()
    }
}