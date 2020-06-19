package com.buffup.app

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.buffup.app.di.appModules
import com.buffup.sdk.BuffSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        BuffSdk.init(
            applicationContext,
            "{Replace with your SDK KEY}",
                    "{Replace with your SDK SECRET}"
        )
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(applicationContext)
            modules(appModules)
        }
    }

}