package com.lecture_list.application.module

import com.lecture_list.data.LectureListRepository
import com.lecture_list.view.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    @Singleton
    @Provides
    fun provideMainViewModel(lectureListRepository: LectureListRepository):
            MainViewModel = MainViewModel(lectureListRepository)
}