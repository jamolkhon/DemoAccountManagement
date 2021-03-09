package com.jamolkhon.assessment.accmgm.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Transaction::class, Balance::class], views = [TransactionWithBalance::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun transactionDao(): TransactionDao

  abstract fun balanceDao(): BalanceDao
}
