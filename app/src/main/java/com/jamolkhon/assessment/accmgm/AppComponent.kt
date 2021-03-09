package com.jamolkhon.assessment.accmgm

import android.content.Context
import com.jamolkhon.assessment.accmgm.api.AccountApi
import com.jamolkhon.assessment.accmgm.main.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): AppComponent
  }

  fun accountApi(): AccountApi

  fun mainViewModelFactory(): MainViewModel.Factory
}
