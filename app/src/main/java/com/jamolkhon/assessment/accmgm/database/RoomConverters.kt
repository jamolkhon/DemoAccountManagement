package com.jamolkhon.assessment.accmgm.database

import androidx.room.TypeConverter
import com.jamolkhon.assessment.accmgm.TransactionStatus
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object RoomConverters {

  private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

  @TypeConverter
  @JvmStatic
  fun toOffsetDateTime(value: String?): OffsetDateTime? = value?.let {
    return formatter.parse(value, OffsetDateTime::from)
  }

  @TypeConverter
  @JvmStatic
  fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

  @TypeConverter
  @JvmStatic
  fun toTransactionStatus(code: Int?): TransactionStatus? = TransactionStatus.of(code)

  @TypeConverter
  @JvmStatic
  fun fromTransactionStatus(status: TransactionStatus?): Int? = status?.code
}
