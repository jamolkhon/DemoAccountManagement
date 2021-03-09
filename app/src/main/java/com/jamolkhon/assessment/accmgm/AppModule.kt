package com.jamolkhon.assessment.accmgm

import android.content.Context
import androidx.room.Room
import com.jamolkhon.assessment.accmgm.api.AccountApi
import com.jamolkhon.assessment.accmgm.database.AppDatabase
import com.jamolkhon.assessment.accmgm.database.BalanceDao
import com.jamolkhon.assessment.accmgm.database.TransactionDao
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Module
  companion object {

    @Provides
    @JvmStatic
    fun transactionDao(database: AppDatabase): TransactionDao = database.transactionDao()

    @Provides
    @JvmStatic
    fun balanceDao(database: AppDatabase): BalanceDao = database.balanceDao()

    @Provides
    @JvmStatic
    @Singleton
    fun appDatabase(context: Context): AppDatabase =
      Room.databaseBuilder(context, AppDatabase::class.java, "db").build()

    @Provides
    @JvmStatic
    @Singleton
    fun accountApi(retrofit: Retrofit): AccountApi = retrofit.create(AccountApi::class.java)

    @Provides
    @JvmStatic
    @Singleton
    fun retrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
      .baseUrl("http://052ddf96e968.ngrok.io/") // todo
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()

    @Provides
    @JvmStatic
    @Singleton
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
      .addNetworkInterceptor { chain ->
        val req = chain.request()
        val newReq = req.newBuilder()
          .removeHeader("Content-Type")
          .addHeader("Content-Type", "application/json") // server cannot parse "application/json; charset=UTF-8"
          .build()
        chain.proceed(newReq)
      }
      .build()

    @Provides
    @JvmStatic
    @Singleton
    fun moshi(): Moshi = Moshi.Builder().build()
  }
}
