package com.jamolkhon.assessment.accmgm

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import kotlin.coroutines.cancellation.CancellationException

var View.visible: Boolean
  get() = visibility == View.VISIBLE
  set(visible) {
    visibility = if (visible) View.VISIBLE else View.GONE
  }

inline fun <reified T : ViewModel> SavedStateRegistryOwner.createAbstractSavedStateViewModelFactory(
  arguments: Bundle,
  crossinline creator: (SavedStateHandle) -> T
): ViewModelProvider.Factory {
  return object : AbstractSavedStateViewModelFactory(this, arguments) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
      key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T = creator(handle) as T
  }
}

inline fun <reified T : ViewModel> Fragment.savedStateViewModels(
  bundle: Bundle = arguments ?: Bundle(),
  crossinline creator: (SavedStateHandle) -> T
): Lazy<T> {
  return createViewModelLazy(T::class, storeProducer = {
    viewModelStore
  }, factoryProducer = {
    createAbstractSavedStateViewModelFactory(bundle, creator)
  })
}

inline fun <reified T : ViewModel> FragmentActivity.savedStateViewModels(
  bundle: Bundle = Bundle(),
  crossinline creator: (SavedStateHandle) -> T
): Lazy<T> {
  return viewModels {
    createAbstractSavedStateViewModelFactory(bundle, creator)
  }
}

fun Exception.rethrowCancellation() {
  if (this is CancellationException) {
    throw this
  }
}
