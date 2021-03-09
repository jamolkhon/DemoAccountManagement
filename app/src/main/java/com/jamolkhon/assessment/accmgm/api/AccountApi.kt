package com.jamolkhon.assessment.accmgm.api

import retrofit2.Response
import retrofit2.http.*

interface AccountApi {

  @POST("/amount")
  suspend fun updateAmount(
    @Header("Transaction-Id") transactionId: String,
    @Body amount: AmountDto
  ): Response<Unit>

  @GET("/balance/{account_id}")
  suspend fun getBalance(
    @Path("account_id") accountId: String
  ): Response<BalanceDto>

  @GET("/transaction/{transaction_id}")
  suspend fun getTransaction(
    @Path("transaction_id") transactionId: String
  ): Response<TransactionDto>
}
