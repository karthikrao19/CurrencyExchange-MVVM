package com.krtk.currencyexchange.presentationLayer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.ExchangeScreen
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.ExchangeUiState
import com.krtk.currencyexchange.presentationLayer.viewModel.ExchangeVM


@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Exchange.route
) {
    val viewModel: ExchangeVM = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Exchange.route) {
            ExchangeScreen(
                viewModel = viewModel,
                effectFlow = viewModel.currentEffect,
                onEventSent = {
                    viewModel.onEventReceived(it)
                }
            )
        }

    }


}