package com.krtk.currencyexchange.dataLayer.remote

import com.krtk.currencyexchange.dataLayer.model.OpenExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //https://openexchangerates.org/api/latest.json?app_id=b96fea102cf04451b74519758d79cfd4
    @GET("latest.json?")
    suspend fun getOpenExchangeRate(@Query("app_id") apiKey: String): Response<OpenExchangeRateResponse>

    companion object {
        const val BASE_URL = "https://openexchangerates.org/api/"
    }
}