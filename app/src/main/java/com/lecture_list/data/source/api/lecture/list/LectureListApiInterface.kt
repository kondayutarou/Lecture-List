package com.lecture_list.data.source.api.lecture.list

import com.lecture_list.model.LectureListItemApi
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface LectureListApiInterface {
    @GET("api/courses/")
    fun fetchLectureList(): Observable<List<LectureListItemApi>>
}