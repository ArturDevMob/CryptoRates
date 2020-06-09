package com.arturdevmob.cryptorates.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.cryptorates.business.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider

class CoinModelFactory(
    private val schedulerProvider: SchedulerProvider,
    private val coinRepository: CoinRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinViewModel::class.java)) {
            return CoinViewModel(schedulerProvider, coinRepository) as T
        } else {
            throw IllegalArgumentException("Неизвестный класс модели: ${modelClass.name}")
        }
    }
}