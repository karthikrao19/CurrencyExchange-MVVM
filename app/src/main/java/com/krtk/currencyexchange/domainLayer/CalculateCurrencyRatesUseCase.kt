package com.krtk.currencyexchange.domainLayer


import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import javax.inject.Inject

class CalculateCurrencyRatesUseCase
@Inject constructor(
    private val repository: ExchangeRepository
) : SafeComputationCallUseCase {

    companion object {
        private val exception = IllegalStateException("Has not enough data to compute rates")
    }

    //The getRatesForCurrency function appears to be responsible for calculating currency exchange rates for
    // a specific target currency in comparison to a list of other currencies.
    suspend fun getRatesForCurrency(
        currency: Currency,
        rates: List<CurrencyRate>,
        currencies: List<Currency>
    ): Result<List<CurrencyRate>> {
        val map = rates.associateBy { it.symbol }.mapValues { it.value.rate }

        return computationCall {
            computeCurrencyRates(map, currencies, currency)
        }
    }

    //The computeCurrencyRates function to be responsible for calculating currency exchange rates
    // relative to a specific target currency, using precomputed dollar value conversions.
    private fun computeCurrencyRates(
        dollarValueMap: Map<String, Double>,
        availableCurrencies: List<Currency>,
        currentCurrency: Currency
    ): List<CurrencyRate> {

        val targetToUSD = dollarValueMap[currentCurrency.symbol]
            ?: throw exception
        val rates = mutableListOf<CurrencyRate>()

        availableCurrencies.forEach {
            if (it != currentCurrency) {
                val currentToUsd = dollarValueMap[it.symbol] ?: 1.0
                val rateValue = (targetToUSD / currentToUsd).roundTo(3)

                rates.add(
                    CurrencyRate(it.symbol, rateValue)
                )
            }
        }

        if (rates.isEmpty()) throw exception

        return rates
    }
}