package com.jamolkhon.assessment.accmgm.database

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.jamolkhon.assessment.accmgm.TransactionStatus
import java.time.OffsetDateTime

@DatabaseView(
  viewName = "transaction_with_balance",
  value = "select t.id, t.account_id, t.amount, t.added, t.status, b.balance from transactions t left join balances b on t.account_id == b.account_id"
)
data class TransactionWithBalance(
  val id: String,
  @ColumnInfo(name = "account_id") val accountId: String,
  val amount: Long,
  val added: OffsetDateTime,
  val status: TransactionStatus,
  val balance: Long = 0
)
