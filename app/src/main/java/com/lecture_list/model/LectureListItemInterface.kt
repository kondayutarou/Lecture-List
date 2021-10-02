package com.lecture_list.model

interface LectureListItemInterface {
    val id: String
    val name: String
    val iconUrl: String
    val numberOfTopics: Int
    val teacherName: String
    val lastAttemptedTs: Int
    var bookmarked: Boolean
    var progress: Int?
}