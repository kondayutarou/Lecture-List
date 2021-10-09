package com.lecture_list.application.module

import android.content.Context
import androidx.room.Room
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.data.source.local.LectureListItemDao
import com.lecture_list.data.source.local.LectureLocalRepository
import com.lecture_list.data.source.local.LectureLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideLectureListItemDao(appDatabase: AppDatabase): LectureListItemDao {
        return appDatabase.lectureListItemDao()
    }

    @Singleton
    @Provides
    fun provideLocalRepository(
        dao: LectureListItemDao
    ): LectureLocalRepository {
        return LectureLocalRepositoryImpl(dao)
    }
}