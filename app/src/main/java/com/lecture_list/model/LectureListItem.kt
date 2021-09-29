package com.lecture_list.model

data class LectureListItem(
    val id: String,
    val name: String,
    val iconUrl: String,
    val numberOfTopics: Int,
    val teacherName: String,
    val last_attempted_ts: Int
)