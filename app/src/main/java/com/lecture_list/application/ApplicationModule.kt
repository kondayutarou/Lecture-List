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
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

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
    fun provideDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMainViewModel(db: AppDatabase, apiRepository: LectureListApiRepositoryInterface):
            MainViewModel {
        return MainViewModel(db, apiRepository)
    }

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
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
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