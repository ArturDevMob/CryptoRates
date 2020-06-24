package com.arturdevmob.cryptorates.domain.usecase

import com.arturdevmob.cryptorates.domain.repositories.CoinRepository

class GetCurrencyUseCase(private val coinRepository: CoinRepository) {
    fun execute() : Array<String> {
        return coinRepository.getCurrencies()
    }
}