package com.arturdevmob.cryptorates.presentation.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider
import com.arturdevmob.cryptorates.domain.models.CoinRateDomainModel
import com.arturdevmob.cryptorates.domain.usecase.GetCoinRatesUseCase
import com.arturdevmob.cryptorates.domain.usecase.GetCurrencyUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CoinRatesViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val getCoinRatesUseCase: GetCoinRatesUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {
    val coinRates = MutableLiveData<CoinRates>() // Модель
    val toSymbols = MutableLiveData<Array<String>>()  // Валюты, в которые можно конвертировать курс
    private var selectedToSymbol = "" // Текущая выбранная валюта для конвертации курса
    private val compositeDisposable = CompositeDisposable()
    private lateinit var currentCoinsDisposable: Disposable

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun changeSelectedToSymbol(toSymbol: String) {
        if (selectedToSymbol != toSymbol) {
            selectedToSymbol = toSymbol
            loadRateCoins(toSymbol)
        }
    }

    fun init() {
        if (coinRates.value == null) {
            toSymbols.value = getCurrencyUseCase.execute()

            toSymbols.value?.let {
                selectedToSymbol = it[0]
                loadRateCoins(selectedToSymbol)
            }
        }
    }

    // Перезапуск потока возвращающий список с криптовалютами
    fun refreshConnection(toSymbol: String) {
        loadRateCoins(toSymbol)
    }

    // Загрузка списка криптовалют с курсом к валюте из параметра
    private fun loadRateCoins(toSymbol: String) {
        // При смене валюты в которую конвертировались криптовалюты
        // нужно отписаться от прошлой подписки на поток
        // (т.к. данные приходят в режиме онлайн, а не однократно)
        if (this::currentCoinsDisposable.isInitialized) {
            currentCoinsDisposable.dispose()
        }

        coinRates.value = CoinRates.Loading

        // Поток
        currentCoinsDisposable = getCoinRatesUseCase.execute(toSymbol)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                when {
                    it.isNotEmpty() && it.first().dbId == null -> {
                        coinRates.value = CoinRates.FromNetwork(it)
                    }
                    it.isNotEmpty() && it.first().dbId != null -> {
                        coinRates.value = CoinRates.FromDb(it)
                    }
                    else -> {
                        coinRates.value = CoinRates.Empty
                    }
                }
            }, {
                it.printStackTrace()
            })

        compositeDisposable.add(currentCoinsDisposable)
    }

    sealed class CoinRates {
        object Loading : CoinRates()
        object Empty : CoinRates()
        class FromNetwork(val data: List<CoinRateDomainModel>) : CoinRates()
        class FromDb(val data: List<CoinRateDomainModel>) : CoinRates()
    }
}