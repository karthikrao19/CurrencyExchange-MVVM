package com.krtk.currencyexchange.domainLayer

import com.krtk.currencyexchange.R
import com.krtk.currencyexchange.common.ApiResult
import com.krtk.currencyexchange.common.Utils
import com.krtk.currencyexchange.dataLayer.local.RoomDao
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.dataLayer.model.OpenExchangeRateResponse
import com.krtk.currencyexchange.dataLayer.model.TimeStamp
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.ExchangeUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

//The domain layer contains the business logic of the application.
// It defines entities and use cases that represent the core functionalities.
interface ExchangeRepository {


    val roomDao: RoomDao
    val viewState: MutableStateFlow<ExchangeUiState>
        get() = MutableStateFlow(ExchangeUiState(loading = true))


    //To insert Currency to DB
    suspend fun insertCurrency(rate: List<CurrencyRate>) {
        roomDao.insertCurrency(rate)
    }

    //To insert Timestamp to DB
    suspend fun insertTimeStamp(timeStamp: TimeStamp) {
        roomDao.insertTimeStamp(timeStamp)
    }

    //To fetch CurrencyRate from DB
    suspend fun getCurrency() : List<CurrencyRate> {
        return  roomDao.getCurrentExchangeRatesDB()
    }

    //To fetch Currency Symbol from DB
    suspend fun getCurrencySymbol() : List<Currency> {
        return  roomDao.getCurrentExchangeSymbol()
    }

    //To fetch TimeStamp from DB
    suspend fun getTimeStamp() : TimeStamp{
        return  roomDao.getTimeStamp()
    }


    suspend fun getCurrentExchangeRate(): Flow<ApiResult<OpenExchangeRateResponse>>

    //The getExchangeRate function appears to be responsible for fetching currency exchange rates
    // from an API and updating the data in a database.
    suspend fun getExchangeRate() {

        getCurrentExchangeRate().collect { response ->
            when (response) {
                is ApiResult.Error -> viewState.update {
                    it.copy(loading = false, error = R.string.error_loading_rates)
                }

                is ApiResult.Loading -> viewState.update {
                    it.copy(loading = true)
                }

                is ApiResult.Success -> {

                    response.data.rates?.let { exchangeRateApi ->
                        val availableRate = exchangeRateApi.map { (key, value) ->
                             CurrencyRate(key, value)
                         }.toMutableList()


                        val timeStamp = TimeStamp(timestamp = Utils.displayTimeStamp(), timestampFormat = Utils.formatTimeStamp(Utils.displayTimeStamp()))
                        insertTimeStamp(timeStamp)
                        insertCurrency(availableRate)

                    }

                }
            }
        }

    }



}

