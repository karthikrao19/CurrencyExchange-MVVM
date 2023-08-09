package com.krtk.currencyexchange.presentationLayer.uiComponents.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.domainLayer.roundTo

@ExperimentalFoundationApi
@Composable
fun CurrenciesAmountsGrid(
    amounts: List<Amount>,
    isError: Boolean,
    isRefreshing: Boolean
) {
    LazyVerticalGrid(
        GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isError) {
            items(1) {
                Box {
                    Text(text = "Network error")
                }
            }
        } else {
            items(amounts.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.inversePrimary
                            )
                            .padding(2.dp, 2.dp, 2.dp, 2.dp)

                    ) {
                        Column( modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = amounts[it].currencyRate.symbol , textAlign = TextAlign.Center)
                            Text(text =  " (" + amounts[it].currencyRate.rate.roundTo(4) + ")", textAlign = TextAlign.Center,
                                fontSize = 12.sp)
                            Text(text = "${amounts[it].amount.roundTo(4)}", textAlign = TextAlign.Center,
                                fontSize = 15.sp,  color = Color.Red )
                        }
                    }
                }
            }
        }
    }

    if (isRefreshing) CircularProgressIndicator()
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CurrenciesAmountsScreenPreview() {
    val currencies = listOf(
        CurrencyRate("USD", 1.0),
        CurrencyRate("EUR", 2.0),
        CurrencyRate("JPY", 3.0),
        CurrencyRate("GBP", 4.0),
        CurrencyRate("EBP", 5.0),
        CurrencyRate("KBP", 6.0),
        // Add more currencies as needed...
    )

    val amounts = currencies.map { currencyRate ->
        Amount(currencyRate, 100.0) // Replace 100.0 with the actual amount you want to display
    }

    Column {
        Text("CurrenciesAmountsGrid:")
        CurrenciesAmountsGrid(
            amounts = amounts, // Pass the list of 'Amount' objects
            isError = false,
            isRefreshing = false
        )
    }
}
