package com.lecture_list.model

import com.lecture_list.extension.mapNotNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

data class StoreValueWithApiError<out T>(
    val value: T,
    val error: ApiError?,
) {
    companion object {
        fun <T> withValue(value: T): StoreValueWithApiError<T> =
            StoreValueWithApiError(value, null)
    }

    override fun toString(): String =
        "StoreValueWithApiError[value: $value, error: $error]"
}

fun <T : Any> Observable<StoreValueWithApiError<T>>.onSuccess(): Observable<T> =
    mapNotNull { it.value }

fun <T : Any> Flowable<StoreValueWithApiError<T>>.onSuccess(): Flowable<T> =
    mapNotNull { it.value }

fun <T : Any> Observable<StoreValueWithApiError<T>>.onError(): Observable<ApiError> =
    mapNotNull { it.error }

fun <T : Any> Flowable<StoreValueWithApiError<T>>.onError(): Flowable<ApiError> =
    mapNotNull { it.error }