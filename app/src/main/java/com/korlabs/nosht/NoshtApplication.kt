package com.korlabs.nosht

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltAndroidApp
class NoshtApplication : Application(){
    companion object {
        lateinit var appContext: NoshtApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}