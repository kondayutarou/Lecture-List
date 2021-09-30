package com.lecture_list.model

import com.squareup.moshi.Json

data class LectureProgressApiItem(
    @Json(name = "course_id")
    val id: String,
    val progress: Int
)