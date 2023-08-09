package com.krtk.currencyexchange.presentationLayer.viewModel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.krtk.currencyexchange.common.Utils
import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.domainLayer.CalculateAmountsUseCase
import com.krtk.currencyexchange.domainLayer.CalculateCurrencyRatesUseCase
import com.krtk.currencyexchange.domainLayer.ExchangeRepository
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.Effect
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.Event
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.ExchangeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//The ViewModel will handle the communication between the Model and the View.
// It exposes data to the View and processes user interactions.
@HiltViewModel
class ExchangeVM @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
    private val currencyRatesComputer: CalculateCurrencyRatesUseCase,
    private val amountComputer: CalculateAmountsUseCase,

    ) : BaseViewModel<
        ExchangeUiState,
        Event,
        Effect>() {

    private val _uiState = MutableStateFlow(ExchangeUiState(loading = true))
    val uiState: StateFlow<ExchangeUiState> = _uiState.asStateFlow()
    private lateinit var countDownTimer: CountDownTimer
    private val _remainingTime = MutableStateFlow(0L)
    val remainingTime: StateFlow<Long>
        get() = _remainingTime


    init {
        Log.i("onEventReceived", "ExchangeVM--.getExchangeRate*******")

        getExchangeRateServerOrDB()
    }

    private fun getExchangeRateServerOrDB() {
        viewModelScope.launch {

            val currencyRateList = exchangeRepository.getCurrency()
            Log.i("ExchangeVM*launch", "getExchangeRate-" + currencyRateList.size)
            if (currencyRateList.isEmpty() || _uiState.value.isRefreshing) {
                exchangeRepository.getExchangeRate()
            }

            Log.i("ExchangeVM*launch", "getExchangeRate-Update-" + currencyRateList.size)

            // get from DB
            val currencyList = exchangeRepository.getCurrency()
            val  amounts = currencyList.map { Amount(it, _uiState.value.baseAmount) }

            _uiState.value = ExchangeUiState(
                currencies = exchangeRepository.getCurrencySymbol(),
                rates = exchangeRepository.getCurrency(),
                amounts = amounts,
                loading = false,
                error = null,
                baseAmount = _uiState.value.baseAmount,
                currentCurrency =  _uiState.value.currentCurrency,
            )

            getAmounts()

        }
    }

    override fun onEventReceived(event: Event) {
        Log.i("onEventReceived", "onEventReceived*******" + _uiState.value.isRefreshing)
        when (event) {
            is Event.AmountChanging -> {
                setNewAmount(event.newAmount)
                Log.i("onEventReceived", "Event.AmountChanging*******" + event.newAmount)

            }

            is Event.CurrencySelection -> {
                Log.i("onEventReceived", "Event.CurrencySelection*******" + event.newCurrency)
                setNewCurrency(event.newCurrency)
            }

            is Event.Refreshing -> {
                _uiState.value.isRefreshing = true
                //_uiState.value.currentCurrency =
                getExchangeRateServerOrDB()
                Log.i("onEventReceived", "Event.Refreshing*******" +  _uiState.value.currentCurrency)
            }
        }
    }

    private fun listIsEmptyUpdateFromDB() {
        viewModelScope.launch {
            if (_uiState.value.rates.isEmpty() || _uiState.value.currencies.isEmpty() ||
                !Utils.isWithin30Minutes(exchangeRepository.getTimeStamp().timestamp)) {

                if (!Utils.isWithin30Minutes(exchangeRepository.getTimeStamp().timestamp)) {
                    _uiState.value.isRefreshing = true
                }
                getExchangeRateServerOrDB()
            }
        }
    }

    private fun setNewAmount(amount: Double) {
        viewModelScope.launch {
            listIsEmptyUpdateFromDB()
        }
        _uiState.value.baseAmount = amount
        getAmounts()
    }



    private fun setNewCurrency(currency: Currency) {
        _uiState.value.currentCurrency = currency
        viewModelScope.launch {
            listIsEmptyUpdateFromDB()
        }

        getCurrencyRates()
    }


    override fun setInitialState(): ExchangeUiState {
        return ExchangeUiState(
            currentCurrency = Currency("USD"),
            currencies = emptyList(),
            rates = emptyList(),
            isRefreshing = true,
            isError = false
        )
    }

    private fun getAmounts() {
        viewModelScope.launch {
            Log.i("getAmounts**", "baseCurrencyAmount-->" + _uiState.value.currentCurrency + " - " +  _uiState.value.baseAmount)
            Log.i("getAmounts**", "currencyRates--->" + " - " +  _uiState.value.rates.size)

            amountComputer.getAmounts(

                baseCurrencyAmount = _uiState.value.baseAmount,
                currencyRates = _uiState.value.rates
            )
                .onSuccess {

                    val nextState = _uiState.value.copy(amounts = it, isRefreshing = false)
                    _uiState.value = nextState
                }
                .onFailure {
                    val nextState = _uiState.value.copy(isError = true, isRefreshing = false)
                    _uiState.value = nextState
                }
        }

    }


    private fun getCurrencyRates() {
        Log.i("getCurrencyRates**", "getCurrencyRates-->" + _uiState.value.currentCurrency + " - " +  _uiState.value.baseAmount)

        viewModelScope.launch {
            currencyRatesComputer.getRatesForCurrency(
                _uiState.value.currentCurrency,
                _uiState.value.rates,
                _uiState.value.currencies)
                .onSuccess {
                    val nextState = _uiState.value.copy(rates = it, isRefreshing = false)
                    _uiState.value = nextState
                    getAmounts()
                }
                .onFailure {
                    val nextState = _uiState.value.copy(isError = true, isRefreshing = false)
                    _uiState.value = nextState
                }
        }
    }
}

