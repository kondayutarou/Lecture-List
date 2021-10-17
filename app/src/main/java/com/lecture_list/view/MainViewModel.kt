package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.lecture_list.data.LectureListRepository
import com.lecture_list.model.LectureListItem
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: LectureListRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val loading = PublishRelay.create<Boolean>()
    val lectureList = BehaviorRelay.create<List<LectureListItem>>()

    fun start() {
        getList()
    }

    fun getList() {
        repository.getLectureList().subscribe({
            lectureList.accept(it.value)
            loading.accept(false)
            it.value.forEach { getProgress(it.id) }
        }, {
            Timber.d(it.toString())
            loading.accept(false)
        })
            .addTo(compositeDisposable)
    }

    fun getProgress(id: String) {
        repository.getProgress(id).subscribe({ apiItem ->
            val modifiedValue = lectureList.value?.toMutableList() ?: return@subscribe
            modifiedValue.map {
                if (it.id == apiItem.value.id) {
                    it.progress = apiItem.value.progress
                    it.progressError = false
                }
            }
            lectureList.accept(modifiedValue)
        }, {
            Timber.d(it.toString())
        })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}