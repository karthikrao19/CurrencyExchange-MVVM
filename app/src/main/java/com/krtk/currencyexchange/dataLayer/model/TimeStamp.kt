package com.krtk.currencyexchange.dataLayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timestamps")
data class TimeStamp(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val timestamp: Long,
    val timestampFormat: String,
)
