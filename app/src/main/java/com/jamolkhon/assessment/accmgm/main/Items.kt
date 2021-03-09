package com.jamolkhon.assessment.accmgm.main

sealed class Items {

  object NewTransaction : Items()

  object TransactionsHeader : Items()

  data class TransactionItem(
    val id: String,
    val accountId: String,
    val amount: Long,
    val balance: Long,
    val failed: Boolean
  ) : Items()
}
