package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepositoryInterface
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.model.LectureList
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val db: AppDatabase, private val apiRepository: LectureListApiRepositoryInterface
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val lectureListItem = BehaviorRelay.create<LectureList>()

    fun start() {
        // TODO: Error handling
        apiRepository.fetchLectureListObservable()
            .subscribeBy(onNext = {
                Logger.d(it.toString())
            }, onError = {
                Logger.d(it.localizedMessage)
            })
            .addTo(compositeDisposable)
    }

    fun finish() {
        compositeDisposable.clear()
    }
}