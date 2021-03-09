package com.jamolkhon.assessment.accmgm.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AmountDto(
  @Json(name = "account_id") val accountId: String,
  val amount: Long
)
