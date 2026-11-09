package com.ua.pavliyk.pager.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {
    single {
        KSafe(
            baseDir = File(System.getProperty("user.home"), ".posts_pager"),
            config = KSafeConfig(appNamespace = "com.ua.pavliyk.pager"),
        )
    }
}
