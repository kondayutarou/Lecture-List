package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepositoryInterface
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.model.LectureList
import com.lecture_list.model.LectureListItem
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val db: AppDatabase, private val apiRepository: LectureListApiRepositoryInterface
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val lectureListItem = BehaviorRelay.create<List<LectureListItem>>()
    // Notify the view of its data change
    val shouldUpdateView = PublishRelay.create<Unit>()

    fun loadApi() {
        // TODO: Error handling
        apiRepository.fetchLectureListObservable()
            .subscribeBy(onNext = {
                lectureListItem.accept(it)
                shouldUpdateView.accept(Unit)
            }, onError = {
                Logger.d(it.localizedMessage)
            })
            .addTo(compositeDisposable)
    }

    fun finish() {
        compositeDisposable.clear()
    }
}