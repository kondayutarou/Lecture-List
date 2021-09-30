package com.lecture_list.model

data class LectureListItem(
    val id: String,
    val name: String,
    val iconUrl: String,
    val numberOfTopics: Int,
    val teacherName: String,
    val lastAttemptedTs: Int,
    var bookmarked: Boolean = false,
    var progress: Int = 0
) {
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

    fun updateProgress(progress: Int): LectureListItem {
        this.progress = progress
        return this
    }

    fun updateBookmarkState(bookmarked: Boolean): LectureListItem {
        this.bookmarked = bookmarked
        return this
    }
}