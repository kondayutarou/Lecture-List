package com.lecture_list.data

import com.lecture_list.data.source.api.lecture.list.LectureListRemoteRepository
import com.lecture_list.data.source.api.lecture.progress.LectureProgressRemoteRepository
import com.lecture_list.data.source.local.LectureLocalRepository
import com.lecture_list.model.LectureListItem
import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Default implementation of [LectureListRepository]. Single entry point for managing lectures' data.
 */
class LectureListRepositoryImpl @Inject constructor(
    private val localRepository: LectureLocalRepository,
    private val lectureListRemoteRepository: LectureListRemoteRepository,
    private val lectureProgressRemoteRepository: LectureProgressRemoteRepository
) : LectureListRepository {
    override fun getLectureList(): Single<List<LectureListItem>> {
        return lectureListRemoteRepository.fetchLectureListObservable()
            .map { list -> list.map { it.toModel() } }
    }

    override fun getProgress(): Single<LectureProgressApiItem> {
        TODO("Not yet implemented")
    }

    override fun saveLectureList(list: List<LectureListItem>): Completable {
        return localRepository.saveList(list)
    }

    override fun saveProgress(): Completable {
        TODO("Not yet implemented")
    }
}
