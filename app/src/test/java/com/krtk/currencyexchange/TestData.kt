package com.krtk.currencyexchange

import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.dataLayer.model.OpenExchangeRateResponse
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.ExchangeUiState

object TestData {
    val currencyRatesResponse =
        OpenExchangeRateResponse(
            base = "USD",
            rates = mapOf(
                "USD" to 1.0,
                "JPY" to 0.007,
                "EUR" to 1.0
            )
        )

    val currenciesList = listOf(
        Currency("USD"),
        Currency("JPY"),
        Currency("EUR")
    )

    val currencyRateList = listOf(
        CurrencyRate("USD", 1.0),
        CurrencyRate("JPY", 0.007),
        CurrencyRate("EUR", 1.0)
    )

    val currencyRatesToJPY = listOf(
        CurrencyRate("USD", 0.007),
        CurrencyRate("EUR", 0.007)
    )

    val currencyJPY = Currency("JPY")
    val currencyUSD = Currency("USD")

    val amountsToOneJPY = listOf(
        Amount(CurrencyRate("USD", 0.007), 0.007),
        Amount(CurrencyRate("EUR", 0.007), 0.007)
    )

    val actualState = ExchangeUiState(
        baseAmount = 1.0,
        currentCurrency = currencyJPY,
        currencies = currenciesList,
        rates = currencyRatesToJPY,
        amounts = amountsToOneJPY,
        isError = false,
        isRefreshing = false
    )

    val actualStateFail = ExchangeUiState(
        baseAmount = 1.0,
        currentCurrency = currencyJPY,
        currencies = currenciesList,
        rates = currencyRatesToJPY,
        amounts = amountsToOneJPY,
        isError = true,
        isRefreshing = false
    )

    val initialState = ExchangeUiState(
        baseAmount = 1.0,
        currentCurrency = currencyJPY,
        currencies = emptyList(),
        rates = emptyList(),
        isError = false,
        isRefreshing = true
    )

}