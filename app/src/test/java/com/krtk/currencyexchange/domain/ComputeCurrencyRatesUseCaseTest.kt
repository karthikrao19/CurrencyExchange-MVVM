package com.krtk.currencyexchange.domain


import com.krtk.currencyexchange.TestData
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.domainLayer.CalculateCurrencyRatesUseCase
import com.krtk.currencyexchange.domainLayer.ExchangeRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ComputeCurrencyRatesUseCaseTest {

    private val repo = mockk<ExchangeRepository>()
    private val dollarValueMap = mockk<MutableMap<String, Double>>()
    private val useCase = CalculateCurrencyRatesUseCase(repo)
    private val availableCurrencies = mockk<List<Currency>>()


    @Test
    fun `should be success`() {
        every { dollarValueMap.toMap() }.returns(hashMapOf())
        every {availableCurrencies.toList()}.returns(listOf(TestData.currencyJPY))


        val result: Result<List<CurrencyRate>>?

        result = runBlocking {
            useCase.getRatesForCurrency(
                TestData.currencyJPY,
                TestData.currencyRateList,
                TestData.currenciesList
            )
        }

        assert(result.isSuccess)
    }

    @Test
    fun `should fail if map is empty`() {
        every { dollarValueMap.toMap() }.returns(hashMapOf())
        every {availableCurrencies.toList()}.returns(listOf(TestData.currencyJPY))


        val result: Result<List<CurrencyRate>>?

        runBlocking {
            result = useCase.getRatesForCurrency(
                TestData.currencyJPY,
                TestData.currencyRatesToJPY,
                TestData.currenciesList
            )
        }


        assert(result!!.isFailure)
    }

    @Test
    fun `should fail if available currencies list is empty`() {
        every { dollarValueMap.toMap()  }.returns(
            hashMapOf("USD" to 1.0)
        )

        every { availableCurrencies.toList() }.returns(emptyList())

        var result: Result<List<CurrencyRate>>?

        runBlocking {
            result = runBlocking {
                useCase.getRatesForCurrency(
                    TestData.currencyJPY,
                    TestData.currencyRatesToJPY,
                    TestData.currenciesList

                )
            }
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should compute currency rates`() {
        val expectedMap = HashMap<String, Double>().apply {
            putAll(TestData.currencyRatesResponse.rates)
        }
        val expectedRates: List<CurrencyRate> = TestData.currencyRatesResponse.rates.entries.map { (symbol, rate) ->
            CurrencyRate(symbol, rate)
        }
        val expectedList = TestData.currenciesList
        val expectedResult = Result.success(TestData.currencyRatesToJPY)

        every { dollarValueMap.toMap() }.returns(expectedMap)
        every { availableCurrencies.toList() }.returns(expectedList)

        var result: Result<List<CurrencyRate>>?

        runBlocking {
            result =
                useCase.getRatesForCurrency(
                    TestData.currencyJPY,
                    expectedRates,
                    expectedList
                )

        }

        assertEquals(result, expectedResult)
    }


}