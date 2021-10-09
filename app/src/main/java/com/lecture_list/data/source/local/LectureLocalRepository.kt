package com.lecture_list.data.source.local

import io.reactivex.rxjava3.core.Completable

interface LectureLocalRepository {
    fun saveList(): Completable

    fun loadList(): Completable
}