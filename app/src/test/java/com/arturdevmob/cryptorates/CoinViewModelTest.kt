package com.arturdevmob.cryptorates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.arturdevmob.cryptorates.business.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.Resource
import com.arturdevmob.cryptorates.data.StatusNetwork
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider
import com.arturdevmob.cryptorates.presentation.models.CoinViewModel
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class CoinViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var coinRepository: CoinRepository
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var coinViewModel: CoinViewModel

    // Observers
    private val oCoins = mock<Observer<Resource<MutableList<CoinEntity>>>>()
    private val oConnectedToServer = mock<Observer<Boolean>>()
    private val oShowEmptyCoinsMessage = mock<Observer<Boolean>>()
    private val oLoadingData = mock<Observer<Boolean>>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = object : SchedulerProvider {
            override fun io(): Scheduler = Schedulers.trampoline()
            override fun ui(): Scheduler = Schedulers.trampoline()
        }
        coinViewModel = CoinViewModel(schedulerProvider, coinRepository)

        `when`(coinRepository.getSelectedToSymbolDefault()).thenReturn(getSelectedToSymbolDefault())

        coinViewModel.loadingData.observeForever(oLoadingData)
        coinViewModel.showEmptyCoinsMessage.observeForever(oShowEmptyCoinsMessage)
        coinViewModel.connectedToServer.observeForever(oConnectedToServer)
        coinViewModel.coinsResource.observeForever(oCoins)
    }

    @Test
    fun `Successful data loading from network`() {
        val coinsResourceSuccess = Resource(StatusNetwork.SUCCESS, createCoinsList())

        `when`(coinRepository.getRateTopCoins(getSelectedToSymbolDefault())).thenReturn(
            Flowable.just(coinsResourceSuccess)
        )

        coinViewModel.startLoadRateTopCoins()

        verify(coinRepository).getToSymbols()
        verify(coinRepository).getSelectedToSymbolDefault()
        verify(oLoadingData).onChanged(true)
        verify(oLoadingData).onChanged(false)
        verify(oShowEmptyCoinsMessage, never()).onChanged(ArgumentMatchers.any())
        verify(oConnectedToServer, never()).onChanged(ArgumentMatchers.anyBoolean())
        verify(oCoins).onChanged(coinsResourceSuccess)
    }

    @Test
    fun `Failed to load data from the network, received data from the cache`() {
        val coinsResourceErrorWithDataFromCache = Resource(StatusNetwork.ERROR, createCoinsList())

        `when`(coinRepository.getRateTopCoins(getSelectedToSymbolDefault())).thenReturn(
            Flowable.just(coinsResourceErrorWithDataFromCache)
        )

        coinViewModel.startLoadRateTopCoins()

        verify(coinRepository).getToSymbols()
        verify(coinRepository).getSelectedToSymbolDefault()
        verify(oLoadingData).onChanged(true)
        verify(oLoadingData).onChanged(false)
        verify(oShowEmptyCoinsMessage, never()).onChanged(ArgumentMatchers.any())
        verify(oConnectedToServer).onChanged(false)
        verify(oCoins).onChanged(coinsResourceErrorWithDataFromCache)
    }

    @Test
    fun `Failed to load data from the network and cache`() {
        val coinsResourceError =
            Resource<MutableList<CoinEntity>>(StatusNetwork.ERROR, mutableListOf())

        `when`(coinRepository.getRateTopCoins(getSelectedToSymbolDefault())).thenReturn(
            Flowable.just(coinsResourceError)
        )

        coinViewModel.startLoadRateTopCoins()

        verify(coinRepository).getToSymbols()
        verify(coinRepository).getSelectedToSymbolDefault()
        verify(oLoadingData).onChanged(true)
        verify(oLoadingData).onChanged(false)
        verify(oShowEmptyCoinsMessage).onChanged(true)
        verify(oConnectedToServer).onChanged(false)
        verify(oCoins).onChanged(coinsResourceError)
    }

    @Test
    fun `Currency change for currency conversion`() {
        val coinsResourceToSymbolUsd = Resource(StatusNetwork.SUCCESS, createCoinsList("USD"))
        val coinsResourceToSymbolRub = Resource(StatusNetwork.SUCCESS, createCoinsList("RUB"))

        `when`(coinRepository.getRateTopCoins(ArgumentMatchers.anyString())).thenReturn(
            Flowable.just(coinsResourceToSymbolUsd),
            Flowable.just(coinsResourceToSymbolRub)
        )

        coinViewModel.startLoadRateTopCoins()
        coinViewModel.changeSelectedToSymbol("RUB")

        verify(coinRepository).getToSymbols()
        verify(coinRepository).getSelectedToSymbolDefault()
        verify(oLoadingData, times(2)).onChanged(true)
        verify(oLoadingData, times(2)).onChanged(false)
        verify(oShowEmptyCoinsMessage, never()).onChanged(ArgumentMatchers.any())
        verify(oConnectedToServer, never()).onChanged(ArgumentMatchers.anyBoolean())
        verify(oCoins).onChanged(coinsResourceToSymbolUsd)
        verify(oCoins).onChanged(coinsResourceToSymbolRub)
    }

    private fun getSelectedToSymbolDefault(): String {
        return "USD"
    }

    private fun createCoinsList(toSymbol: String = getSelectedToSymbolDefault()): MutableList<CoinEntity> {
        val coins = mutableListOf<CoinEntity>()

        coins.add(
            CoinEntity("BTC", toSymbol, 1.2, 3.4, 5.6, "url")
        )
        coins.add(
            CoinEntity("ETH", toSymbol, 11.22, 33.33, 44.44, "url")
        )

        return coins
    }

    private inline fun <reified T> mock() = mock(T::class.java)
}