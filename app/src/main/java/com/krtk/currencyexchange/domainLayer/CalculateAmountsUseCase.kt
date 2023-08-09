package com.krtk.currencyexchange.domainLayer

import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import javax.inject.Inject

class CalculateAmountsUseCase
@Inject constructor() : SafeComputationCallUseCase {

    companion object {
        private val exception = IllegalArgumentException("Empty currency rates list")
    }

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

    private fun computeCurrencyAmounts(
        baseCurrencyAmount: Double,
        currencyRates: List<CurrencyRate>
    ): List<Amount> {
        return currencyRates.map {
            Amount(it, computeAmount(it.rate, baseCurrencyAmount).roundTo(3))
        }
    }

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