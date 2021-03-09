package com.jamolkhon.assessment.accmgm.database

import androidx.room.*
import androidx.room.Transaction

@Dao
abstract class BalanceDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  abstract suspend fun insert(balance: Balance): Long

  @Update
  abstract suspend fun update(balance: Balance)

  @Transaction
  open suspend fun insertOrUpdate(balance: Balance) {
    val id = insert(balance)
    if (id == -1L) {
      update(balance)
    }
  }
}
