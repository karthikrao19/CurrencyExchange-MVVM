package com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen

import androidx.annotation.StringRes
import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.dataLayer.model.OpenExchangeRateResponse
import com.krtk.currencyexchange.presentationLayer.viewModel.ViewEffect
import com.krtk.currencyexchange.presentationLayer.viewModel.ViewEvent
import com.krtk.currencyexchange.presentationLayer.viewModel.ViewState

data class ExchangeUiState(
    var baseAmount: Double = 1.0,
    val loading: Boolean = false,
    @StringRes val error: Int? = null,
    var currentCurrency: Currency = Currency("USD"),
    val currencies: List<Currency> = emptyList(),
    val rates: List<CurrencyRate> = emptyList(),
    val amounts: List<Amount> = emptyList(),
    val isError: Boolean = false,
    var isRefreshing: Boolean = false,

    ): ViewState


sealed class Event : ViewEvent {
    data class AmountChanging(val newAmount: Double) : Event()

    data class CurrencySelection(val newCurrency: Currency) : Event()

    object Refreshing : Event()
}

sealed class Effect : ViewEffect {

    sealed class NetworkCall : Effect() {

        object Failed : NetworkCall()

        object Succeeded : NetworkCall()
    }
}
