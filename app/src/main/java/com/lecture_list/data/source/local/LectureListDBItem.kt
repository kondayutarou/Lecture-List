package com.lecture_list.data.source.local

import androidx.room.*
import com.lecture_list.model.LectureListItem
import com.lecture_list.model.LectureListItemInterface

@Entity
data class LectureListDBItem(
    @PrimaryKey override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "iconUrl") override val iconUrl: String,
    @ColumnInfo(name = "numberOfTopics") override val numberOfTopics: Int,
    @ColumnInfo(name = "teacherName") override val teacherName: String,
    @ColumnInfo(name = "lastAttemptedTs") override val lastAttemptedTs: Int,
    @ColumnInfo(name = "bookmarked") override var bookmarked: Boolean,
    @ColumnInfo(name = "progress") override var progress: Int?
) : LectureListItemInterface {
    fun toModel(): LectureListItem {
        return LectureListItem(
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
