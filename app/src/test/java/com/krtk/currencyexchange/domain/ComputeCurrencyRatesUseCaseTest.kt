package com.krtk.currencyexchange.domain


import com.krtk.currencyexchange.TestData
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

    private val useCase = CalculateCurrencyRatesUseCase(repo)


    @Test
    fun `should fail if map is empty`() = runBlocking {
        val currency = repo.getCurrency()
        every {currency }.returns(    emptyList())

        val currencySymbol = repo.getCurrencySymbol()

        every { currencySymbol }.returns(
            listOf(TestData.currencyJPY)
        )


        val result: Result<List<CurrencyRate>>?

        result = runBlocking {
            useCase.getRatesForCurrency(
                TestData.currencyJPY,
                repo.getCurrency(),
                repo.getCurrencySymbol()
            )
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should fail if available currencies list is empty`()= runBlocking {
        val currency = repo.getCurrency()
        every { currency }.returns(
            emptyList()
        )

        val currencySymbol = repo.getCurrencySymbol()

        every { currencySymbol }.returns(emptyList())

        var result: Result<List<CurrencyRate>>?

        runBlocking {
            //result = useCase.getRatesForCurrency(TestData.currencyJPY)
            result = runBlocking {
                useCase.getRatesForCurrency(
                    TestData.currencyJPY,
                    repo.getCurrency(),
                    currencySymbol
                )
            }
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should compute currency rates`() = runBlocking {
        val expectedMap = HashMap<String, Double>().apply {
            putAll(TestData.currencyRatesResponse.rates)
        }
        val expectedList = TestData.currenciesList
        val expectedResult = Result.success(TestData.currencyRatesToJPY)

        //every { repo.getDollarValueMap() }.returns(expectedMap)
       // every { repo.getAvailableCurrenciesStored() }.returns(expectedList)

        val currencyList = repo.getCurrency()


        every { currencyList }.returns(
            TestData.currencyRateList
        )

        val currencySymbol = repo.getCurrencySymbol()

        every { currencySymbol }.returns(
            listOf(TestData.currencyJPY)
        )

        var result: Result<List<CurrencyRate>>?

        runBlocking {
            //result = useCase.getRatesForCurrency(TestData.currencyJPY)
            result = runBlocking {
                useCase.getRatesForCurrency(
                    TestData.currencyJPY,
                    repo.getCurrency(),
                    currencySymbol
                )
            }
        }

        assertEquals(result, expectedResult)
    }


}