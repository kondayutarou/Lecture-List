package com.lecture_list.data.source.api.lecture.progress

import com.lecture_list.model.LectureProgressApiItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LectureProgressApiRepositoryInterface {
    fun fetchLectureListObservable(courseId: String): Single<LectureProgressApiItem>
}