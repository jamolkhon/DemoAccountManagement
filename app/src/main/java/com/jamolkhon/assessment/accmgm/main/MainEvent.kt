package com.jamolkhon.assessment.accmgm.main

sealed class MainEvent {

  object InvalidAccountId : MainEvent()

  object InvalidAmount : MainEvent()

  object SubmitFailed : MainEvent()

  object TransactionNotSaved : MainEvent()

  object BalanceUpdateFailed : MainEvent()
}
