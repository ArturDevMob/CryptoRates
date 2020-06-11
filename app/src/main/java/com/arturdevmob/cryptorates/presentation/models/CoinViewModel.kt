package com.arturdevmob.cryptorates.presentation.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arturdevmob.cryptorates.business.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.Resource
import com.arturdevmob.cryptorates.data.StatusNetwork
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CoinViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val coinRepository: CoinRepository
) : ViewModel() {
    val coinsResource = MutableLiveData<Resource<MutableList<CoinEntity>>>() // Список криптовалют
    val connectedToServer = MutableLiveData<Boolean>() // Состояние подключение к серверу
    val showEmptyCoinsMessage = MutableLiveData<Boolean>() // Список с криптовалютами пуст
    val loadingData = MutableLiveData<Boolean>() // Данные в процессе загрузки
    private val compositeDisposable = CompositeDisposable()
    private lateinit var currentCoinsDisposable: Disposable // Поток с текущими криптовалютами
    lateinit var selectedToSymbol: String // Выбранная валюта для конвертации
    lateinit var toSymbols: Array<String> // Доступные валюты для конвертации

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun changeSelectedToSymbol(toSymbol: String) {
        selectedToSymbol = toSymbol
        loadRateTopCoins()
    }

    fun startLoadRateTopCoins() {
        if (coinsResource.value == null) {
            selectedToSymbol = coinRepository.getSelectedToSymbolDefault()
            toSymbols = coinRepository.getToSymbols()

            loadRateTopCoins()
        }
    }

    // Перезапуск потока возвращающий список с криптовалютами
    fun refreshConnection() {
        loadRateTopCoins()
    }

    // Загрузка списка криптовалют с курсом к валюте из параметра
    private fun loadRateTopCoins() {
        loadingData.value = true

        // Прерываем прошлый поток
        // (т.к. данные приходят в режиме онлайн, а не однократно)
        if (this::currentCoinsDisposable.isInitialized) {
            currentCoinsDisposable.dispose()
        }

        // Поток
        currentCoinsDisposable = coinRepository.getRateTopCoins(selectedToSymbol)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                when (it.status) {
                    StatusNetwork.SUCCESS -> {
                        connectedToServer.value?.let { boolean ->
                            if (boolean.not()) {
                                // Соединение с сервером восстановлено
                                connectedToServer.value = true
                                showEmptyCoinsMessage.value = false
                            }
                        }
                    }
                    StatusNetwork.ERROR -> {
                        connectedToServer.value = false // Соединения с сервером нет

                        if (it.data.isEmpty()) {
                            // В БД данных тоже нет, показать заглушку
                            showEmptyCoinsMessage.value = true
                        }
                    }
                }

                loadingData.value = false
                coinsResource.value = it
            }, {
                it.printStackTrace()
            })

        compositeDisposable.add(currentCoinsDisposable)
    }
}