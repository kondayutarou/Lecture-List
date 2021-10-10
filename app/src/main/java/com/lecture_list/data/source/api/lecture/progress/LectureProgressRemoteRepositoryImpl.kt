package com.lecture_list.data.source.api.lecture.progress

import com.lecture_list.model.StoreValueWithApiError
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class LectureProgressRemoteRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : LectureProgressRemoteRepository {
    override fun fetchLectureListObservable(courseId: String): @NonNull Single<StoreValueWithApiError<LectureProgressApiItem>> {
        return retrofit.create(LectureProgressApiInterface::class.java)
            .fetchLectureProgress(courseId)
            .map { apiItem -> StoreValueWithApiError(apiItem, null) }
            .subscribeOn(Schedulers.io())
    }
}