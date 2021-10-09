package com.lecture_list.application.module

import android.content.Context
import com.lecture_list.data.LectureListRepository
import com.lecture_list.data.LectureListRepositoryImpl
import com.lecture_list.data.source.api.lecture.list.LectureListRemoteRepository
import com.lecture_list.data.source.api.lecture.progress.LectureProgressRemoteRepository
import com.lecture_list.data.source.local.LectureLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    companion object {
        const val BASE_URL = "base_url"
    }

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideLectureListRepository(
        localRepository: LectureLocalRepository,
        lectureListRemoteRepository: LectureListRemoteRepository,
        lectureProgressRemoteRepository: LectureProgressRemoteRepository
    ): LectureListRepository {
        return LectureListRepositoryImpl(
            localRepository,
            lectureListRemoteRepository,
            lectureProgressRemoteRepository
        )
    }
}