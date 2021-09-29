package com.lecture_list.application

import android.content.Context
import androidx.room.Room
import com.lecture_list.BuildConfig
import com.lecture_list.R
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepository
import com.lecture_list.data.source.api.lecture.list.LectureListApiRepositoryInterface
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.view.MainViewModel
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
class ApplicationModule {
    companion object {
        const val BASE_URL = "base_url"
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMainViewModel(@ApplicationContext context: Context): MainViewModel {
        return MainViewModel(provideDb(context))
    }

    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLogger = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLogger.setLevel(HttpLoggingInterceptor.Level.BASIC)
        } else {
            httpLogger.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return httpLogger
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLogger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLogger)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    @Named(BASE_URL)
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
        @Named(BASE_URL) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideLectureListApiRepository(
        retrofit: Retrofit
    ): LectureListApiRepositoryInterface {
        return LectureListApiRepository(retrofit)
    }
}