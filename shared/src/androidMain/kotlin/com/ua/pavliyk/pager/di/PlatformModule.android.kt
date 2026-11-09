package com.ua.pavliyk.pager.di

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val platformModule = module {
    single { KSafe(androidApplication()) }
}
