# CurrencyExchange
A sample Android application built to demonstrate the use of Modern Android development tools - (Jetpack Compose, Material You, Kotlin, Coroutines, Flow, Hilt, Architecture Components, MVVM, Retrofit, Moshi)

# MVVM Architecture Android Beginners: Sample App

This repository contains a sample app that implements MVVM architecture using Kotlin, Dagger,
Retrofit, Coroutines, Flow, StateFlow, and etc.
<p align="center">
   <img src="https://github.com/karthikrao19/CurrencyExchange/assets/3007975/27620c87-6140-42df-82d2-eeda32fdcd4a">
</p>
<br>
<p align="center">
    <img src="https://github.com/karthikrao19/CurrencyExchange/assets/3007975/1b9e23d4-1562-4ba1-8ca8-2d834e7a8687">
</p>
<br>
<p align="center">
  <img src="https://github.com/karthikrao19/CurrencyExchange/assets/3007975/4d893e0b-fa7d-4e48-b265-375646204703">
</p>
<br>
<br>


### Major Highlights

- MVVM Architecture
- Kotlin
- Dagger
- Retrofit
- Coroutines
- Flow
- StateFlow

### The app has following packages:

- **data**: It contains all the data accessing and manipulating components.
- **di**: It contains all the dependency injection related classes and interfaces.
- **ui**: View classes along with their corresponding ViewModel.
- **utils**: Utility classes.

### Resources to learn MVVM Architecture and other components used in this project:

- MVVM
  Architecture:
    - We will cover the following in this tutorial:
        - What is MVVM architecture?
        - Set up a new project with Kotlin and other dependencies required.
        - Project Structure.
        - Set up the utils package.
        - Set up the data layer.
        - Set up the di layer.
        - Set up UI layer, build and run the project.
        - Project Source Code and What Next?
- Mastering Kotlin
  Coroutines:
- Flow API in Kotlin
- Learn Kotlin Topics

<p align="center">
  <img src="https://github.com/karthikrao19/CurrencyExchange/assets/3007975/3e797719-e66d-4184-92fc-6317429b6bd7">
</p>



## Description:
Simple currency converter application, which uses free version of *openexchangerates.org* API.
//https://docs.openexchangerates.org/reference/api-introduction
Implemented features:
- Fetching currencies
- Fetching and computing rates for available currencies
- Caching of fetched data
- Conversion from any available currency to other one

## To do: Functional Requirements
● The required data must be fetched from the open exchange rates service.
○ See the documentation for information on how to use their API.
○ You must use a free account - not a paid one.
○ Get a free App ID that will give you access to the Open Exchange Rates API
here.

● The required data must be persisted locally to permit the application to be used
offline after data has been fetched.
● In order to limit bandwidth usage, the required data can be refreshed from the API no
more frequently than once every 30 minutes.
● The user must be able to select a currency from a list of currencies provided by open
exchange rates.
● The user must be able to enter the desired amount for the selected currency.
● The user must then be shown a list showing the desired amount in the selected
currency converted into amounts in each currency provided by open exchange rates.
○ If exchange rates for the selected currency are not available via open
exchange rates, perform the conversions on the app side.
○ When converting, floating point errors are acceptable.
● The project must contain unit tests that ensure correct operation.
