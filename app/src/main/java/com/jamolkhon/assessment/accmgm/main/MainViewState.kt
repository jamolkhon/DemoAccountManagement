package com.jamolkhon.assessment.accmgm.main

data class MainViewState(
  val submitting: Boolean = false,
  val retrying: Boolean = false,
  val updating: Boolean = false,
  val items: List<Items> = emptyList()
) {
  fun requestInProgress(): Boolean = submitting || retrying || updating
}
