package com.lecture_list.application

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.lecture_list.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationComponent : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .build()

        val logAdapter = object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        }
        Logger.addLogAdapter(logAdapter)
    }
}