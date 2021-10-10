package com.lecture_list.data

import com.lecture_list.data.source.api.lecture.list.LectureListRemoteRepository
import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiItem
import com.lecture_list.data.source.api.lecture.progress.LectureProgressRemoteRepository
import com.lecture_list.data.source.local.LectureLocalRepository
import com.lecture_list.model.ApiError
import com.lecture_list.model.LectureListItem
import com.lecture_list.model.StoreValueWithApiError
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
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
    override fun getLectureList(): @NonNull Single<StoreValueWithApiError<List<LectureListItem>>> {
        return lectureListRemoteRepository.fetchLectureListObservable()
            .doAfterSuccess { list ->
                // Subscription is disposed when completes.
                saveLectureList(list.value).subscribe()
            }
            // Return database value when api call is unsuccessful.
            .onErrorResumeNext { throwable ->
                return@onErrorResumeNext loadLectureList().map {
                    StoreValueWithApiError(it, throwable as ApiError?)
                }
            }
    }

    override fun getProgress(id: String): @NonNull Single<StoreValueWithApiError<LectureProgressApiItem>> {
        return lectureProgressRemoteRepository.fetchLectureListObservable(id)
            .doAfterSuccess { apiItem ->
                // Subscription is disposed when completes.
                saveProgress(apiItem.value).subscribe()
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
