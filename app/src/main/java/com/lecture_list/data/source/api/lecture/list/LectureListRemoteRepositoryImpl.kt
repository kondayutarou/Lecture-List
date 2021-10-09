package com.lecture_list.data.source.api.lecture.list

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class LectureListRemoteRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : LectureListRemoteRepository {
    override fun fetchLectureListObservable(): Single<List<LectureListApiItem>> {
        return retrofit.create(LectureListApiInterface::class.java)
            .fetchLectureList()
            .subscribeOn(Schedulers.io())
    }
}