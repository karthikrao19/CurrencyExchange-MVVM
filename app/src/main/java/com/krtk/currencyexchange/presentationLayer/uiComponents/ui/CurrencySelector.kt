package com.krtk.currencyexchange.presentationLayer.uiComponents.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.Event

@Composable
fun CurrencySelector(
    current: Currency,
    currencies: List<Currency>,
    onEventSent: (Event) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf(current) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd // Aligns the text to the top-right corner
    ) {

        Text(
            text = selectedCurrency.symbol,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable { expanded = true }
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.shapes.medium
                )
                .widthIn(min = 150.dp)
                .padding(10.dp)


        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.align(Alignment.CenterEnd)  // Add padding between text and icon
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEachIndexed { index, currency ->
                DropdownMenuItem(
                    { Text(
                        text = currency.symbol,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()) },
                    onClick = {
                        onEventSent(Event.CurrencySelection(currency))
                        selectedCurrency = currency
                        expanded = false
                    }
                )


                if (index < currencies.size - 1) {
                    Divider(
                        color = Color.LightGray,
                        thickness = 2.dp,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }

        }


    }
}

@Preview
@Composable
fun CurrencySelectorPreview() {
    val currencies = listOf(
        Currency("USD"),
        Currency("EUR"),
        Currency("JPY"),
        Currency("GBP"),
        // Add more currencies as needed...
    )

    CurrencySelector(
        current = currencies[0], // Provide an initial currency
        currencies = currencies
    ) { event ->
        Log.i("onClick", "test")
        // Handle the event here, if needed
    }

}
