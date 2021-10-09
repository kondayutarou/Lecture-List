package com.lecture_list.data.source.api.lecture.list

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface LectureListApiInterface {
    @GET("api/courses/")
    fun fetchLectureList(): Single<List<LectureListApiItem>>
}