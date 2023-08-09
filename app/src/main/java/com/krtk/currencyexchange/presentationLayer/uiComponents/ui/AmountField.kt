package com.krtk.currencyexchange.presentationLayer.uiComponents.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountField(
    onEventSent: (Event) -> Unit
) {
    var amount by remember { mutableStateOf("1.0") }

    OutlinedTextField(
        value = amount,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { newValue ->
            if (newValue.isNotEmpty()) {
                val newDoubleValue = newValue.toDoubleOrNull()
                if (newDoubleValue != null) {
                    onEventSent(Event.AmountChanging(newDoubleValue))
                    amount = newValue
                } else {
                    // Handle invalid input, set default value, or show error message
                    // For example, setting amount to "1.0" as default:
                    onEventSent(Event.AmountChanging(1.0))
                    amount = "1.0"
                }
            } else {
                // Handle empty input, set default value, or show error message
                // For example, setting amount to "1.0" as default:
                onEventSent(Event.AmountChanging(1.0))
                amount = "1.0"
            }
            Log.i("AmountField***>", "AmountField***-->" + amount)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = { /* Handle keyboard done action if needed */ }),
        textStyle = LocalTextStyle.current.copy(color = Color.Black), // Customize text color
    )

}

@Preview
@Composable
fun AmountFieldPreview() {
    var amount by remember { mutableStateOf("0.0") }
    AmountField(onEventSent = { event ->
        if (event is Event.AmountChanging) {
            amount = event.newAmount.toString()
        }
    })
}