package com.krtk.currencyexchange.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.krtk.currencyexchange.dataLayer.model.OpenExchangeRateResponse
import junit.framework.Assert.assertEquals
import org.junit.Test

class ResponseTest {

    private val gson = Gson()

    private val currencyRatesJson =
        "{\"base\": \"USD\", \"rates\": { \"AED\": 3.6731 } }"

    private val expectedCurrencyRatesResponse = OpenExchangeRateResponse(
        base = "USD", rates = mapOf("AED" to 3.6731)
    )

    private val availableCurrenciesJson =
        "{ \"USD\": \"United States Dollar\", \"JPY\": \"Japanese Yen\" }"

    private val expectedAvailableCurrenciesMap = mapOf(
        "USD" to "United States Dollar",
        "JPY" to "Japanese Yen"
    )

    @Test
    fun `should deserialize currency rates response correctly`() {
        assertEquals(
            gson.fromJson(currencyRatesJson, OpenExchangeRateResponse::class.java),
            expectedCurrencyRatesResponse
        )
    }

    @Test
    fun `should deserialize available currencies response correctly`() {
        val type = object : TypeToken<Map<String, String>>() {}.type

        assertEquals(
            gson.fromJson(availableCurrenciesJson, type),
            expectedAvailableCurrenciesMap
        )
    }
}