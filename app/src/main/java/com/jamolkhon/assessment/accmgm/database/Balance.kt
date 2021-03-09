package com.jamolkhon.assessment.accmgm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jamolkhon.assessment.accmgm.TransactionStatus
import java.time.OffsetDateTime

@Entity(tableName = "balances")
data class Balance(
  @PrimaryKey @ColumnInfo(name = "account_id") val accountId: String,
  val balance: Long
)
