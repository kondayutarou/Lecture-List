package com.lecture_list.model

import com.lecture_list.extension.mapNotNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

data class StoreValueWithApiError<out T>(
    val value: T?,
    val error: ApiError?,
) {
    companion object {
        fun <T> withValue(value: T): StoreValueWithApiError<T> =
            StoreValueWithApiError(value, null)

        private fun <T> withError(error: ApiError): StoreValueWithApiError<T> =
            StoreValueWithApiError(null, error)

        fun <T> fromThrowable(t: Throwable): StoreValueWithApiError<T> {
            return withError(t as? ApiError ?: ApiNetworkingError(t))
        }
    }

    override fun toString(): String =
        "StoreValueWithApiError[value: $value, error: $error]"
}

fun <T> Observable<T>.mapToStoreValueWithApiError(): Observable<StoreValueWithApiError<T>> =
    map { StoreValueWithApiError.withValue(it) }
        .onErrorReturn { StoreValueWithApiError.fromThrowable(it) }

fun <T, R> Observable<T>.mapToStoreValueWithApiError(mapper: (T) -> R)
        : Observable<StoreValueWithApiError<R>> =
    map { StoreValueWithApiError.withValue(mapper(it)) }
        .onErrorReturn { StoreValueWithApiError.fromThrowable(it) }

fun <T> Single<T>.mapToStoreValueWithApiError(): Single<StoreValueWithApiError<T>> =
    map { StoreValueWithApiError.withValue(it) }
        .onErrorReturn { StoreValueWithApiError.fromThrowable(it) }

fun <T, R> Single<T>.mapToStoreValueWithApiError(mapper: (T) -> R)
        : Single<StoreValueWithApiError<R>> =
    map { StoreValueWithApiError.withValue(mapper(it)) }
        .onErrorReturn { StoreValueWithApiError.fromThrowable(it) }

fun <T : Any> Observable<StoreValueWithApiError<T>>.onSuccess(): Observable<T> =
    mapNotNull { it.value }

fun <T : Any> Flowable<StoreValueWithApiError<T>>.onSuccess(): Flowable<T> =
    mapNotNull { it.value }

fun <T : Any> Observable<StoreValueWithApiError<T>>.onError(): Observable<ApiError> =
    mapNotNull { it.error }

fun <T : Any> Flowable<StoreValueWithApiError<T>>.onError(): Flowable<ApiError> =
    mapNotNull { it.error }