package com.krtk.currencyexchange.dataLayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates_table")
data class CurrencyRate(
    @PrimaryKey
    val symbol: String,
    val rate: Double
)
