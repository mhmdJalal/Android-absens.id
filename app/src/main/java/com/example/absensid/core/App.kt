package com.example.absensid.core

import android.app.*
import androidx.appcompat.app.AppCompatDelegate
import com.example.absensid.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(appModules))
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}