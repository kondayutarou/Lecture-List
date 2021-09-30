package com.lecture_list.data.source.api.lecture.progress

import com.lecture_list.model.LectureProgressApiItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class LectureProgressApiRepository @Inject constructor(
    private val retrofit: Retrofit
) : LectureProgressApiRepositoryInterface {
    override fun fetchLectureListObservable(courseId: String): Observable<LectureProgressApiItem> {
        return retrofit.create(LectureProgressApiInterface::class.java)
            .fetchLectureProgress(courseId)
            .subscribeOn(Schedulers.io())
    }
}