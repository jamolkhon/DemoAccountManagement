package com.jamolkhon.assessment.accmgm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

  @Query("select * from `transaction_with_balance` order by added desc")
  fun transactions(): Flow<List<TransactionWithBalance>>

  @Query("select * from `transactions` where id = :id")
  suspend fun transactionById(id: String): Transaction

  @Insert
  suspend fun insertTransaction(transaction: Transaction)

  @Update
  suspend fun updateTransaction(transaction: Transaction)
}
