package com.lecture_list.data.source.api.lecture.progress

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface LectureProgressApiInterface {
    @GET("api/{course_id}/usage/")
    fun fetchLectureProgress(
        @Path(value = "course_id") courseId: String
    ): Single<LectureProgressApiItem>
}