package com.lecture_list.model

import com.lecture_list.data.LectureListDB

data class LectureListItem(
    override val id: String,
    override val name: String,
    override val iconUrl: String,
    override val numberOfTopics: Int,
    override val teacherName: String,
    override val lastAttemptedTs: Int,
    override var bookmarked: Boolean = false,
    override var progress: Int? = null
) : LectureListItemInterface {
    companion object {
        // Init LectureListItem from LectureListApi response
        fun fromListApi(listApiItem: LectureListApiItem): LectureListItem {
            return LectureListItem(
                id = listApiItem.id,
                name = listApiItem.name,
                iconUrl = listApiItem.iconUrl,
                numberOfTopics = listApiItem.numberOfTopics,
                teacherName = listApiItem.teacherName,
                lastAttemptedTs = listApiItem.lastAttemptedTs
            )
        }
    }

    fun toDBClass(): LectureListDB {
        return LectureListDB(
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