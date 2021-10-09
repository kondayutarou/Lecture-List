package com.lecture_list.data.source.api.lecture.list

import com.lecture_list.model.LectureListItem
import com.squareup.moshi.Json

data class LectureListApiItem(
    val id: String,
    val name: String,
    @Json(name = "icon_url")
    val iconUrl: String,
    @Json(name = "number_of_topics")
    val numberOfTopics: Int,
    @Json(name = "teacher_name")
    val teacherName: String,
    @Json(name = "last_attempted_ts")
    val lastAttemptedTs: Int
) {
    fun toModel(): LectureListItem {
        return LectureListItem(
            id, name, iconUrl, numberOfTopics, teacherName, lastAttemptedTs,
            progressError = true
        )
    }
}