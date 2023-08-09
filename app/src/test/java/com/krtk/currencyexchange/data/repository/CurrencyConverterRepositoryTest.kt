package com.krtk.currencyexchange.data.repository

import com.krtk.currencyexchange.BuildConfig
import com.krtk.currencyexchange.TestData
import com.krtk.currencyexchange.common.ApiResult
import com.krtk.currencyexchange.dataLayer.local.AppDatabase
import com.krtk.currencyexchange.dataLayer.model.OpenExchangeRateResponse
import com.krtk.currencyexchange.dataLayer.remote.ApiService
import com.krtk.currencyexchange.dataLayer.repository.ExchangeRepositoryImpl
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Test

import retrofit2.Response

class CurrencyConverterRepositoryTest {

    private val service = mockk<ApiService>()
    private val appDatabase = mockk<AppDatabase>()

    private val repo = ExchangeRepositoryImpl(service, appDatabase)

    private val currenciesMap = mapOf(
        "USD" to "United States Dollar",
        "JPY" to "Japanese Yen",
        "EUR" to "Euro"
    )


    @Test
    suspend fun `should call get currency rates and save to map`() {
        coEvery { service.getOpenExchangeRate(apiKey = BuildConfig.TMDB_API_KEY) }
            .returns(Response.success(TestData.currencyRatesResponse))

        runBlocking {
            repo.getCurrentExchangeRate()
        }

        coVerify(exactly = 1) {
            service.getOpenExchangeRate(apiKey = BuildConfig.TMDB_API_KEY)
        }
        assertEquals(repo.getCurrency(), TestData.currencyRatesResponse.rates)
    }

    @Test
    suspend fun `should clear map on success`() {
        coEvery { service.getOpenExchangeRate(apiKey = BuildConfig.TMDB_API_KEY) }
            .returns(Response.success(TestData.currencyRatesResponse))

        // Mock the function to return a success response
        coEvery { repo.getCurrentExchangeRate() }.returns(
            flow { emit(ApiResult.Success(TestData.currencyRatesResponse)) }
        )

        runBlocking {
            repo.getCurrentExchangeRate()
            //repo.getCurrentExchangeRate()
        }
       // assertEquals(repo.getCurrentExchangeRate(), TestData.currencyRatesResponse.rates)
        assertEquals(repo.getCurrency(), TestData.currencyRatesResponse.rates)

    }

    @Test
    suspend fun `should return failure on getCurrencyRates fail`() {
        coEvery { service.getOpenExchangeRate(apiKey = BuildConfig.TMDB_API_KEY) }
            .returns(Response.error(500, ResponseBody.create(null, "")))

        //var result: Result<Unit>?

        // var result : Flow<ApiResult<OpenExchangeRateResponse>>
        var result: Flow<ApiResult<OpenExchangeRateResponse>> = flow {
            emit(ApiResult.Error(Exception("Mocked error response")))
        }

        runBlocking {
            result = repo.getCurrentExchangeRate()

        }

        // assert(result!!.collect() )
        result.collect { apiResult ->
            assertTrue(apiResult is ApiResult.Error)
        }
    }
}