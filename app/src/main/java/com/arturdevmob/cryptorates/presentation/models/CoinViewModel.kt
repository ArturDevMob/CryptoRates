package com.arturdevmob.cryptorates.presentation.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arturdevmob.cryptorates.data.StatusNetworkDataLoad
import com.arturdevmob.cryptorates.data.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CoinViewModel(private val coinRepository: CoinRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var currentCoinsDisposable: Disposable // Поток с текущими криптовалютами
    private val coins = MutableLiveData<MutableList<CoinEntity>>() // Список криптовалют
    private val isConnectedToServer = MutableLiveData<Boolean>() // Состояние подключение к серверу
    private val isShowEmptyCoinsMessage = MutableLiveData<Boolean>() // Список с криптовалютами пуст
    private val isLoadingData = MutableLiveData<Boolean>() // Данные в процессе загрузки
    private var selectedToSymbol = coinRepository.getSelectedToSymbolDefault() // Выбранная валюта для конвертации
    private val toSymbols = coinRepository.getToSymbols() // Доступные валюты для конвертации

    init {
        loadRateTopCoins(selectedToSymbol)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    // Возвращает массив с названиями валют
    // в которые можно конвертировать курс криптовалют
    fun getToSymbols(): Array<String> {
        return toSymbols
    }

    // Возвращает список криптовалютами
    fun getCoins(): LiveData<MutableList<CoinEntity>> {
        return coins
    }

    // Устанавливаем название валюты
    // которая выбранная для конвертирования курса криптовалюты
    fun setSelectedToSymbol(toSymbol: String) {
        if (toSymbol != selectedToSymbol) {
            loadRateTopCoins(toSymbol)
            selectedToSymbol = toSymbol
        }
    }

    // Состояние соединения с сервером
    // false - соединения нет, true - соединение есть
    fun isConnectedToServer(): LiveData<Boolean> {
        return isConnectedToServer
    }

    // Состояние соединения списка с криптовалютами
    // false - список пуст, true - список не пуст
    fun isShowEmptyCoinsMessage(): LiveData<Boolean> {
        return isShowEmptyCoinsMessage
    }

    // Состояние загрузки данных
    // false - в покое, true - загружаются данные
    fun isLoadingData(): LiveData<Boolean> {
        return isLoadingData
    }

    // Перезапуск потока возвращающий список с криптовалютами
    fun refreshConnection() {
        loadRateTopCoins(selectedToSymbol)
    }

    // Загрузка списка криптовалют с курсом к валюте из параметра
    private fun loadRateTopCoins(toSymbol: String) {
        isLoadingData.value = true

        // Прерываем прошлый поток
        // (т.к. данные приходят в режиме онлайн, а не однократно)
        if (::currentCoinsDisposable.isInitialized) {
            currentCoinsDisposable.dispose()
        }

        // Поток
        currentCoinsDisposable = coinRepository.getRateTopCoins(toSymbol)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                when (it.status) {
                    StatusNetworkDataLoad.SUCCESS -> {
                        isLoadingData.value = false
                        coins.value = it.data // Данные пришли из сети

                        isConnectedToServer.value?.let { boolean ->
                            if (boolean.not()) {
                                isConnectedToServer.value = true // Соединение с сервером восстановлено
                                isShowEmptyCoinsMessage.value = false
                            }
                        }
                    }
                    StatusNetworkDataLoad.ERROR -> {
                        isLoadingData.value = false
                        isConnectedToServer.value = false // Соединения с сервером нет

                        if (it.data.isNotEmpty()) {
                            coins.value = it.data // Но данные пришли из бд
                        } else {
                            isShowEmptyCoinsMessage.value = true // В БД данных тоже нет
                        }
                    }
                }
            }, {
                it.printStackTrace()
            })

        compositeDisposable.add(currentCoinsDisposable)
    }
}