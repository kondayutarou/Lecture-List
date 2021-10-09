package com.lecture_list.application

import android.app.Application
import com.lecture_list.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ApplicationComponent : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}