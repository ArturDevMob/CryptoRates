package com.arturdevmob.cryptorates.presentation.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.cryptorates.data.repositories.CoinRepository

class CoinModelFactory(private val coinRepository: CoinRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CoinViewModel(coinRepository) as T
    }
}