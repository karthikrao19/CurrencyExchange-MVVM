package com.krtk.currencyexchange.domain


import com.krtk.currencyexchange.TestData
import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.domainLayer.CalculateAmountsUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ComputeAmountsUseCaseTest {

    private val useCase = CalculateAmountsUseCase()

    @Test
    fun `should return failure when empty list is supplied`() {
        var result: Result<List<Amount>>?

        runBlocking {
            result = useCase.getAmounts(
                baseCurrencyAmount = 1.0,
                currencyRates = emptyList()
            )
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should compute and round amounts correctly`() {
        var result: Result<List<Amount>>?

        runBlocking {
            result = useCase.getAmounts(
                baseCurrencyAmount = 10.0,
                currencyRates = TestData.currencyRateList
            )
        }

        val expectedResult = Result.success(
            listOf(
                Amount(CurrencyRate("USD", 1.0), 10.0),
                Amount(CurrencyRate("JPY", 0.007), 1428.571),
                Amount(CurrencyRate("EUR", 1.0), 10.0)
            )
        )

        assertEquals(result, expectedResult)
    }

}