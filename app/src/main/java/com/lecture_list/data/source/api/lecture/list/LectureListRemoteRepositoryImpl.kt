package com.lecture_list.data.source.api.lecture.list

import com.lecture_list.model.LectureListItem
import com.lecture_list.model.StoreValueWithApiError
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class LectureListRemoteRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : LectureListRemoteRepository {
    override fun fetchLectureListObservable(): @NonNull Single<StoreValueWithApiError<List<LectureListItem>>> {
        return retrofit.create(LectureListApiInterface::class.java)
            .fetchLectureList()
            .map { list -> StoreValueWithApiError(list.map { it.toModel() }, null) }
            .subscribeOn(Schedulers.io())
    }
}