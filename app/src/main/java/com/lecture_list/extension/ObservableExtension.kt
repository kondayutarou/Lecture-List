package com.lecture_list.extension

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

inline fun <T, R : Any> Observable<T>.mapNotNull(crossinline transform: (T) -> R?): Observable<R> {
    return flatMap { t: T ->
        val value = transform(t) ?: return@flatMap Observable.empty<R>()
        Observable.just(value)
    }
}

inline fun <T, R : Any> Flowable<T>.mapNotNull(crossinline transform: (T) -> R?): Flowable<R> {
    return flatMap { t: T ->
        val value = transform(t) ?: return@flatMap Flowable.empty<R>()
        Flowable.just(value)
    }
}