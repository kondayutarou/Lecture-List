package com.lecture_list.data.source.api.lecture.progress

import com.squareup.moshi.Json

data class LectureProgressApiItem(
    @Json(name = "course_id")
    val id: String,
    val progress: Int
)