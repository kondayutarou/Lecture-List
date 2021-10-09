package com.lecture_list.data

import com.lecture_list.data.source.api.lecture.list.LectureListRemoteRepository
import com.lecture_list.data.source.api.lecture.progress.LectureProgressRemoteRepository
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.model.LectureListItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Default implementation of [LectureListRepository]. Single entry point for managing lectures' data.
 */
class LectureListRepositoryImpl@Inject constructor(
    private val db: AppDatabase,
    private val lectureListRemoteRepository: LectureListRemoteRepository,
    private val lectureProgressRemoteRepository: LectureProgressRemoteRepository
): LectureListRepository {
    override fun getLectureList(): Single<List<LectureListItem>> {
        TODO("Not yet implemented")
    }

    override fun saveLectureList(): Completable {
        TODO("Not yet implemented")
    }
}
