package com.lecture_list.data.source.local

import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiItem
import com.lecture_list.model.LectureListItem
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class LectureLocalRepositoryImpl internal constructor(
    private val lectureDao: LectureListItemDao
) : LectureLocalRepository {
    override fun saveList(list: List<LectureListItem>): Completable {
        val deleteCompletable = lectureDao.deleteAll()
        val insertCompletable = lectureDao.insertAll(list.map { it.toDBClass() })

        return deleteCompletable.andThen(insertCompletable).subscribeOn(Schedulers.io())
    }

    override fun saveProgress(apiItem: LectureProgressApiItem): Completable {
        return lectureDao.updateById(apiItem.id, apiItem.progress, false)
    }

    override fun loadList(): Single<List<LectureListDBItem>> {
        return lectureDao.getAll().subscribeOn(Schedulers.io())
    }
}