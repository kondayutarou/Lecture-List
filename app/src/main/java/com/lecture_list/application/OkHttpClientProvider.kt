package com.lecture_list.application

import com.lecture_list.BuildConfig
import com.lecture_list.model.ApiServerError
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpClientProvider {
    fun build(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(getLoggingInterceptor())
        .addInterceptor(::interceptServerStatusCode)
        .build()

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLogger = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLogger.setLevel(HttpLoggingInterceptor.Level.BASIC)
        } else {
            httpLogger.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return httpLogger
    }

    @Throws(ApiServerError::class)
    private fun interceptServerStatusCode(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            throw ApiServerError(response.code, response.message)
        } else {
            return response
        }
    }
}

