package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.lecture_list.data.LectureListRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: LectureListRepository
) : ViewModel() {
    private val dataLoading = BehaviorRelay.create<Boolean>()
    private var dataLoaded = false

    fun start() {
        if (dataLoading.value == true) {
            return
        } else {
            dataLoading.accept(true)
        }

        repository.getLectureList()
    }
}