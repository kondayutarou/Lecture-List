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
        fun fromListApi(listItemApi: LectureListItemApi): LectureListItem {
            return LectureListItem(
                id = listItemApi.id,
                name = listItemApi.name,
                iconUrl = listItemApi.iconUrl,
                numberOfTopics = listItemApi.numberOfTopics,
                teacherName = listItemApi.teacherName,
                lastAttemptedTs = listItemApi.lastAttemptedTs
            )
        }
    }
}