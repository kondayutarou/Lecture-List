package com.lecture_list.data.source.local

import com.lecture_list.model.LectureListItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LectureLocalRepositoryImpl internal constructor(
    private val lectureDao: LectureListItemDao
) : LectureLocalRepository {
    override fun saveList(list: List<LectureListItem>): Completable {
        val deleteCompletable = lectureDao.deleteAll()
        val insertCompletable = lectureDao.insertAll(list.map { it.toDBClass() })

        return deleteCompletable.andThen(insertCompletable)
    }

    override fun loadList(): Single<List<LectureListDBItem>> {
        return lectureDao.getAll()
    }
}