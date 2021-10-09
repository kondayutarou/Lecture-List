package com.lecture_list.model

import com.lecture_list.data.source.local.LectureListDBItem

data class LectureListItem(
    override val id: String,
    override val name: String,
    override val iconUrl: String,
    override val numberOfTopics: Int,
    override val teacherName: String,
    override val lastAttemptedTs: Int,
    override var bookmarked: Boolean = false,
    override var progress: Int? = null,
    var progressError: Boolean = false
) : LectureListItemInterface {
    fun toDBClass(): LectureListDBItem {
        return LectureListDBItem(
            id,
            name,
            iconUrl,
            numberOfTopics,
            teacherName,
            lastAttemptedTs,
            bookmarked,
            progress
        )
    }
}