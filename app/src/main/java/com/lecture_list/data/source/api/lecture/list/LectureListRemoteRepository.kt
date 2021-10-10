package com.lecture_list.data.source.api.lecture.list

import com.lecture_list.model.LectureListItem
import com.lecture_list.model.StoreValueWithApiError
import io.reactivex.rxjava3.core.Single

interface LectureListRemoteRepository {
    fun fetchLectureListObservable(): Single<StoreValueWithApiError<List<LectureListItem>>>
}