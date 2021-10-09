package com.lecture_list.application.module

import android.content.Context
import com.lecture_list.BuildConfig
import com.lecture_list.R
import com.lecture_list.application.OkHttpClientProvider
import com.lecture_list.data.source.api.lecture.list.LectureListRemoteRepository
import com.lecture_list.data.source.api.lecture.list.LectureListRemoteRepositoryImpl
import com.lecture_list.data.source.api.lecture.progress.LectureProgressRemoteRepository
import com.lecture_list.data.source.api.lecture.progress.LectureProgressRemoteRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    // API
    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClientProvider {
        return OkHttpClientProvider()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(okHttpBuilder: OkHttpClientProvider): OkHttpClient {
        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    @Named(ApplicationModule.BASE_URL)
    fun provideBaseUrl(@ApplicationContext context: Context): String {
        return if (BuildConfig.DEBUG) {
            context.getString(R.string.base_url_debug)
        } else {
            context.getString(R.string.base_url_release)
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient, moshi: Moshi,
        @Named(ApplicationModule.BASE_URL) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    // API repositories
    @Singleton
    @Provides
    fun provideLectureListRemoteRepository(
        retrofit: Retrofit
    ): LectureListRemoteRepository {
        return LectureListRemoteRepositoryImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideLectureProgressRemoteRepository(
        retrofit: Retrofit
    ): LectureProgressRemoteRepository {
        return LectureProgressRemoteRepositoryImpl(retrofit)
    }
}