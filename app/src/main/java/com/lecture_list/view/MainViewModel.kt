package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.lecture_list.data.LectureListRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: LectureListRepository
) : ViewModel() {
    fun start() {

    }
}