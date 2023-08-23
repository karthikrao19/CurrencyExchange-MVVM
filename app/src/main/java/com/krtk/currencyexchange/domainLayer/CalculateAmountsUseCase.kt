package com.krtk.currencyexchange.domainLayer

import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import javax.inject.Inject

class CalculateAmountsUseCase
@Inject constructor() : SafeComputationCallUseCase {

    companion object {
        private val exception = IllegalArgumentException("Empty currency rates list")
    }

    //getAmounts function appears to be a part of a larger system that deals with currency conversion.
    // It takes a base currency amount and a list of currency rates as input, and it aims to calculate
    // and return a list of equivalent amounts in different target currencies.
    suspend fun getAmounts(
        baseCurrencyAmount: Double,
        currencyRates: List<CurrencyRate>
    ): Result<List<Amount>> {
        return if (currencyRates.isEmpty()) {
            Result.failure(exception)
        } else {
            computationCall { computeCurrencyAmounts(baseCurrencyAmount, currencyRates) }
        }
    }

// computeCurrencyAmounts that calculates currency amounts in different target currencies based on a given
// base currency amount and a list of currency rates.
    private fun computeCurrencyAmounts(
        baseCurrencyAmount: Double,
        currencyRates: List<CurrencyRate>
    ): List<Amount> {
        return currencyRates.map {
            Amount(it, computeAmount(it.rate, baseCurrencyAmount).roundTo(3))
        }
    }

    //computeAmount function calculates the equivalent amount in a target currency based on a
    // given exchange rate and a base amount in a source currency.
    private fun computeAmount(
        rate: Double,
        base: Double
    ): Double {
        return if (rate >= 1) {
            rate * base
        } else {
            base / rate
        }
    }
}