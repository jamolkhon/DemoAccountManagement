package com.jamolkhon.assessment.accmgm.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionDto(val balance: Long)
