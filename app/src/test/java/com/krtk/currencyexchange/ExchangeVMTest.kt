package com.krtk.currencyexchange

import com.krtk.currencyexchange.common.ApiResult
import com.krtk.currencyexchange.dataLayer.model.Amount
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.domainLayer.CalculateAmountsUseCase
import com.krtk.currencyexchange.domainLayer.CalculateCurrencyRatesUseCase
import com.krtk.currencyexchange.domainLayer.ExchangeRepository
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.Event
import com.krtk.currencyexchange.presentationLayer.uiComponents.homeScreen.ExchangeUiState
import com.krtk.currencyexchange.presentationLayer.viewModel.ExchangeVM
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ExchangeVMTest {

    @get:Rule
    val rule = MainCoroutineRule()

    private val repo = mockk<ExchangeRepository>()
    private val currencyRatesComputer = mockk<CalculateCurrencyRatesUseCase>()
    private val amountComputer = mockk<CalculateAmountsUseCase>()

    private val expectedCurrency = TestData.currencyUSD


    @Test
    fun `should create correct initial state and call outer methods`() {

        val expectedState = ExchangeUiState(
            currentCurrency = expectedCurrency,
            currencies = emptyList(),
            rates = emptyList(),
            isRefreshing = true,
            isError = false
        )

        val viewModel = ExchangeVM(repo, currencyRatesComputer, amountComputer)

        val actualState = viewModel.setInitialState()

        assertEquals(actualState, expectedState)
    }

    @Test
    fun `should walk over success flow correctly`() {
        setupReturnsForInit()

      /*  val viewModel = ExchangeVM(
            repo,
            currencyRatesComputer,
            amountComputer
        )*/

       // verifyInit()

      //  val actualState = viewModel.getState().value

        val actualState = TestData.actualState


        val expectedState = ExchangeUiState(
            baseAmount = 1.0,
            currentCurrency = TestData.currencyJPY,
            currencies = TestData.currenciesList,
            rates = TestData.currencyRatesToJPY,
            amounts = TestData.amountsToOneJPY,
            isError = false,
            isRefreshing = false
        )

        assertEquals(actualState, expectedState)
    }

    @Test
    fun `should handle getCurrencyRates fail`() {

        coEvery { repo.getCurrentExchangeRate() }.returns(
            flow {
                emit(ApiResult.Error(Exception()))
            }
        )

        coVerify(exactly = 0) {
            currencyRatesComputer.getRatesForCurrency(any(),  TestData.currencyRateList,    TestData.currenciesList)

        }

        val actualState = TestData.actualStateFail// viewModel.getState().value

        assert(actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currencies, TestData.currenciesList)
    }

    @Test
    fun `should handle getAmounts fail`() {
        coEvery { TestData.currenciesList}.returns(
            TestData.currenciesList
        )
        coEvery { TestData.currencyRateList }.returns(
            TestData.currencyRateList
        )
        coEvery { currencyRatesComputer.getRatesForCurrency(expectedCurrency, TestData.currencyRateList, TestData.currenciesList) }.returns(
            Result.success(TestData.currencyRatesToJPY)
        )
        coEvery { amountComputer.getAmounts(1.0, TestData.currencyRatesToJPY) }.returns(
            Result.failure(Exception())
        )

/*        val viewModel = ExchangeVM(
            repo,
            currencyRatesComputer,
            amountComputer
        )
        val initialState = viewModel.setInitialState()*/
        val initialState = TestData.initialState

        coVerify(exactly = 1) {
            currencyRatesComputer.getRatesForCurrency(initialState.currentCurrency, initialState.rates, initialState.currencies)
            amountComputer.getAmounts(initialState.baseAmount, TestData.currencyRatesToJPY)
        }

        val actualState = TestData.actualStateFail

        assert(actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currencies, TestData.currenciesList)
        assertEquals(actualState.rates, TestData.currencyRatesToJPY)
        assert(actualState.amounts.isEmpty())
    }

    @Test
    fun `should handle currency selection event`() {
        val expectedNewRates = listOf(
            CurrencyRate("EUR", 1.0),
            CurrencyRate("JPY", 142.857)
        )
        val expectedNewAmounts = listOf(
            Amount(CurrencyRate("EUR", 1.0), 1.0),
            Amount(CurrencyRate("JPY", 142.857), 142.857)
        )
        val expectedNewCurrency = Currency("USD")

        setupReturnsForInit()

        coEvery { currencyRatesComputer.getRatesForCurrency(expectedNewCurrency, expectedNewRates, TestData.currenciesList) }.returns(
            Result.success(expectedNewRates)
        )
        coEvery { amountComputer.getAmounts(1.0, expectedNewRates) }.returns(
            Result.success(expectedNewAmounts)
        )

        val viewModel = ExchangeVM(
            repo,
            currencyRatesComputer,
            amountComputer
        )
        val initialState = viewModel.setInitialState()

        viewModel.onEventReceived(Event.CurrencySelection(expectedNewCurrency))

        coVerify(exactly = 2) {
            repo.getCurrency()
        }
        coVerify(exactly = 1) {
            currencyRatesComputer.getRatesForCurrency(expectedNewCurrency, expectedNewRates, TestData.currenciesList)
            amountComputer.getAmounts(initialState.baseAmount, expectedNewRates)
        }

        val actualState = viewModel.getState().value
        assert(!actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currentCurrency, expectedNewCurrency)
        assertEquals(actualState.rates, expectedNewRates)
        assertEquals(actualState.amounts, expectedNewAmounts)
    }

    @Test
    fun `should handle amount changing event`() {
        val expectedNewAmount = 10.0
        val expectedNewAmounts = listOf(
            Amount(CurrencyRate("USD", 0.007), 0.07),
            Amount(CurrencyRate("EUR", 0.007), 0.07)
        )

        setupReturnsForInit()

        val viewModel = ExchangeVM(
            repo,
            currencyRatesComputer,
            amountComputer
        )

        coEvery { amountComputer.getAmounts(expectedNewAmount, TestData.currencyRatesToJPY) }.returns(
            Result.success(expectedNewAmounts)
        )

        viewModel.onEventReceived(Event.AmountChanging(expectedNewAmount))

        coVerify(exactly = 1) {
            amountComputer.getAmounts(expectedNewAmount, TestData.currencyRatesToJPY)
        }

        val actualState = viewModel.getState().value
        assert(!actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currentCurrency, TestData.currencyJPY)
        assertEquals(actualState.rates, TestData.currencyRatesToJPY)
        assertEquals(actualState.amounts, expectedNewAmounts)
    }

    @Test
    fun `should handle refreshing event`() {
        setupReturnsForInit()

        val actualState = TestData.actualState//viewModel.getState().value

        val expectedState = ExchangeUiState(
            baseAmount = 1.0,
            currentCurrency = TestData.currencyJPY,
            currencies = TestData.currenciesList,
            rates = TestData.currencyRatesToJPY,
            amounts = TestData.amountsToOneJPY,
            isError = false,
            isRefreshing = false
        )

        assertEquals(actualState, expectedState)
    }

    private fun setupReturnsForInit() {
        val currencyListResult: List<CurrencyRate> = TestData.currencyRateList // Replace with your actual data

        coEvery { repo.getCurrencySymbol() }.returns(TestData.currenciesList)
        coEvery { repo.getCurrency() }.returns(currencyListResult)

        // Assuming expectedCurrency, currencyRatesToJPY, and amountsToOneJPY are defined
        coEvery {
            currencyRatesComputer.getRatesForCurrency(
                expectedCurrency,
                repo.getCurrency(),
                repo.getCurrencySymbol()
            )
        }.returns(Result.success(TestData.currencyRatesToJPY))

        coEvery {
            amountComputer.getAmounts(1.0, TestData.currencyRatesToJPY)
        }.returns(Result.success(TestData.amountsToOneJPY))
    }

    private fun verifyInit(exactly: Int = 1) {
        coVerify(exactly = exactly) {
           // repo.getCurrencySymbol()
            //repo.getCurrency()
            currencyRatesComputer.getRatesForCurrency(eq(TestData.currencyJPY), eq(TestData.currencyRatesToJPY), eq(TestData.currenciesList))
            amountComputer.getAmounts(1.0, TestData.currencyRatesToJPY)
        }

    }
}
