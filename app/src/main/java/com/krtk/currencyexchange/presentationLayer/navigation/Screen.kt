package com.krtk.currencyexchange.presentationLayer.navigation

sealed class Screen(val route: String){
    object Exchange : Screen("exchange")
}
