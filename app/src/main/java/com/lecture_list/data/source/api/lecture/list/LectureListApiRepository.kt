package com.lecture_list.data.source.api.lecture.list

import com.lecture_list.model.LectureListApiItem
import com.lecture_list.model.LectureListItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class LectureListApiRepository @Inject constructor(
    private val retrofit: Retrofit
) : LectureListApiRepositoryInterface {
    override fun fetchLectureListObservable(): Single<List<LectureListApiItem>> {
        return retrofit.create(LectureListApiInterface::class.java)
            .fetchLectureList()
            .subscribeOn(Schedulers.io())
    }
}