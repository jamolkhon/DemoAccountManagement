package com.jamolkhon.assessment.accmgm.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalanceDto(val balance: Long)
