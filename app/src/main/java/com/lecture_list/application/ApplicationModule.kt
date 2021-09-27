package com.lecture_list.application

import android.content.Context
import androidx.room.Room
import com.lecture_list.data.source.local.AppDatabase
import com.lecture_list.view.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

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
}