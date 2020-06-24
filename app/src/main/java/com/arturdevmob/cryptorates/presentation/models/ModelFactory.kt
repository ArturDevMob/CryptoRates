package com.arturdevmob.cryptorates.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider
import com.arturdevmob.cryptorates.domain.usecase.GetCoinRatesUseCase
import com.arturdevmob.cryptorates.domain.usecase.GetCurrencyUseCase

class ModelFactory(
    private val schedulerProvider: SchedulerProvider,
    private val getCoinRatesUseCase: GetCoinRatesUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinRatesViewModel::class.java)) {
            return CoinRatesViewModel(schedulerProvider, getCoinRatesUseCase, getCurrencyUseCase) as T
        } else {
            throw IllegalArgumentException("Неизвестный класс модели: ${modelClass.name}")
        }
    }
}