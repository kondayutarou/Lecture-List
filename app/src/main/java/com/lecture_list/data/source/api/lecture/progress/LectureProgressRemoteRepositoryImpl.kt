package com.lecture_list.data.source.api.lecture.progress

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class LectureProgressRemoteRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : LectureProgressRemoteRepository {
    override fun fetchLectureListObservable(courseId: String): Single<LectureProgressApiItem> {
        return retrofit.create(LectureProgressApiInterface::class.java)
            .fetchLectureProgress(courseId)
            .subscribeOn(Schedulers.io())
    }
}