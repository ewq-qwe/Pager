package com.ua.pavliyk.pager

import android.app.Application
import com.ua.pavliyk.pager.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AppApplication)
            androidLogger()
        }
    }
}