package com.krtk.currencyexchange.dataLayer.repository

import com.krtk.currencyexchange.BuildConfig
import com.krtk.currencyexchange.common.ApiResult
import com.krtk.currencyexchange.dataLayer.local.AppDatabase
import com.krtk.currencyexchange.dataLayer.local.RoomDao
import com.krtk.currencyexchange.dataLayer.remote.SafeApiRequest
import com.krtk.currencyexchange.dataLayer.model.OpenExchangeRateResponse
import com.krtk.currencyexchange.dataLayer.remote.ApiService
import com.krtk.currencyexchange.domainLayer.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRepositoryImpl  @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,

) : ExchangeRepository {

    override val roomDao: RoomDao = appDatabase.roomDao()



    override suspend fun getCurrentExchangeRate(): Flow<ApiResult<OpenExchangeRateResponse>> =
        SafeApiRequest.apiRequest {
            apiService.getOpenExchangeRate(BuildConfig.TMDB_API_KEY)
        }
}