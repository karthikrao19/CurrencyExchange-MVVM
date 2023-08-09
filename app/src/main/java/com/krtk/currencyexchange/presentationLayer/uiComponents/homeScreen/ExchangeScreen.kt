package com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krtk.currencyexchange.presentationLayer.common.Header
import com.krtk.currencyexchange.R
import com.krtk.currencyexchange.presentationLayer.common.Loader
import com.krtk.currencyexchange.presentationLayer.uiComponents.ui.AmountField
import com.krtk.currencyexchange.presentationLayer.uiComponents.ui.CurrenciesAmountsGrid
import com.krtk.currencyexchange.presentationLayer.uiComponents.ui.CurrencySelector
import com.krtk.currencyexchange.presentationLayer.viewModel.ExchangeVM
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExchangeScreen (
    viewModel: ExchangeVM,
    effectFlow: Flow<Effect>?,
    onEventSent: (Event) -> Unit,

) {


    val state: ExchangeUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            text = stringResource(id = R.string.liveExchangeRate),
            modifier = Modifier.padding(8.dp)
        )

        if (state.loading) {
            Loader()
        } else if (state.error == null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp, 0.dp),

                ) {
                AmountField(
                    onEventSent = { event -> onEventSent(event) }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Button(
                        onClick = { onEventSent(Event.Refreshing) }
                    ) {
                        Text(text = "Refresh")
                    }

                    CurrencySelector(
                        current = state.currentCurrency,
                        currencies = state.currencies,
                        onEventSent = onEventSent

                    )
                }

                CurrenciesAmountsGrid(
                    amounts = state.amounts,
                    isError = state.isError,
                    isRefreshing = state.isRefreshing
                )
            }
        }
    }


}

