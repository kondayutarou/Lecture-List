package com.lecture_list.data

import com.lecture_list.data.source.api.lecture.list.LectureListRemoteRepository
import com.lecture_list.data.source.api.lecture.progress.LectureProgressRemoteRepository
import com.lecture_list.data.source.local.LectureLocalRepository
import com.lecture_list.model.LectureListItem
import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiItem
import com.lecture_list.model.ApiNetworkingError
import com.lecture_list.model.ApiServerError
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Default implementation of [LectureListRepository]. Single entry point for managing lectures' data.
 * Each repository returns observable with the io scheduler set, so there's no need to set scheduler here.
 */
class LectureListRepositoryImpl @Inject constructor(
    private val localRepository: LectureLocalRepository,
    private val lectureListRemoteRepository: LectureListRemoteRepository,
    private val lectureProgressRemoteRepository: LectureProgressRemoteRepository
) : LectureListRepository {
    override fun getLectureList(): Single<List<LectureListItem>> {
        return lectureListRemoteRepository.fetchLectureListObservable()
            .map { list -> list.map { it.toModel() } }
            .doAfterSuccess { list ->
                // Subscription is disposed when completes.
                saveLectureList(list).subscribe()
            }
            // Return database value when api call is unsuccessful.
            .onErrorResumeNext {
                return@onErrorResumeNext loadLectureList()
            }
    }

    override fun getProgress(id: String): Single<LectureProgressApiItem> {
        return lectureProgressRemoteRepository.fetchLectureListObservable(id)
            .doAfterSuccess { apiItem ->
                // Subscription is disposed when completes.
                saveProgress(apiItem).subscribe()
            }
    }

    override fun saveProgress(progressItem: LectureProgressApiItem): Completable {
        return localRepository.saveProgress(progressItem)
    }

    override fun saveLectureList(list: List<LectureListItem>): Completable {
        return localRepository.saveList(list)
    }

    override fun loadLectureList(): Single<List<LectureListItem>> {
        return localRepository.loadList().map { list -> list.map { it.toModel() } }
    }
}
