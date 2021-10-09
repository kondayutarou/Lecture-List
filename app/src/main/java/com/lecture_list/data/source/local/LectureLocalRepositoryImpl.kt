package com.lecture_list.data.source.local

import io.reactivex.rxjava3.core.Completable

class LectureLocalRepositoryImpl internal constructor(
    private val lectureDao: LectureListItemDao
) : LectureLocalRepository {
    override fun saveList(): Completable {
        TODO("Not yet implemented")
    }

    override fun loadList(): Completable {
        TODO("Not yet implemented")
    }
}