package com.dirzaaulia.perqararawg

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        //Timber
        Timber.plant(Timber.DebugTree())

        //Material You
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}