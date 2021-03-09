package com.jamolkhon.assessment.accmgm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jamolkhon.assessment.accmgm.TransactionStatus
import java.time.OffsetDateTime

@Entity(tableName = "transactions")
data class Transaction(
  @PrimaryKey val id: String,
  @ColumnInfo(name = "account_id") val accountId: String,
  val amount: Long,
  val added: OffsetDateTime,
  val status: TransactionStatus
)
