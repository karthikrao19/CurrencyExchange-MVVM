package com.krtk.currencyexchange.presentationLayer.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.krtk.currencyexchange.R

@Composable
@Preview
fun Header(modifier: Modifier = Modifier, text: String = stringResource(id = R.string.app_name)) {
    Text(modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleLarge)
}