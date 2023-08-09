package com.krtk.currencyexchange.dataLayer.model


data class OpenExchangeRateResponse(
    val base: String,
    val rates: Map<String, Double>

)

