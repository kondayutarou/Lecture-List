package com.lecture_list.data.source.api.lecture.progress

import com.lecture_list.model.StoreValueWithApiError
import io.reactivex.rxjava3.core.Single

interface LectureProgressRemoteRepository {
    fun fetchLectureListObservable(courseId: String): Single<StoreValueWithApiError<LectureProgressApiItem>>
}