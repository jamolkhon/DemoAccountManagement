package com.jamolkhon.assessment.accmgm

enum class TransactionStatus(val code: Int) {

  COMPLETED(0), FAILED(1);

  companion object {
    fun of(code: Int?): TransactionStatus? = when (code) {
      COMPLETED.code -> COMPLETED
      FAILED.code -> FAILED
      else -> null
    }
  }
}
