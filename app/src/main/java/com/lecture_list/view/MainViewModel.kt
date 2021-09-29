package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.orhanobut.logger.Logger
import com.lecture_list.data.ShoppingListItem
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepositoryInterface
import com.lecture_list.data.source.local.AppDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val db: AppDatabase, private val apiRepository: LectureListApiRepositoryInterface
) : ViewModel() {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun start() {
    }

    fun finish() {
        compositeDisposable.clear()
    }
}