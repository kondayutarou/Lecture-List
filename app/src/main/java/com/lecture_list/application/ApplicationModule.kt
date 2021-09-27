package com.lecture_list.application

import android.content.Context
import androidx.room.Room
import com.lecture_list.BuildConfig
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.view.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(ActivityComponent::class)
class ApplicationModule {
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    fun provideMainViewModel(@ApplicationContext context: Context): MainViewModel {
        return MainViewModel(provideDb(context))
    }

    @Singleton
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
    fun provideOkHttpClient(httpLogger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLogger)
            .build()
    }
}