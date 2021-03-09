package com.jamolkhon.assessment.accmgm

import android.app.Application
import timber.log.Timber

class App : Application() {

  lateinit var component: AppComponent

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    component = DaggerAppComponent.factory().create(this)
  }
}
