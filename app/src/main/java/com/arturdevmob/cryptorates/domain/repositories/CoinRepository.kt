package com.arturdevmob.cryptorates.domain.repositories

import com.arturdevmob.cryptorates.domain.models.CoinRateDomainModel
import io.reactivex.Flowable

interface CoinRepository {
    // Валюты, в которые будет конвертироваться курс криптовалюты
    fun getCurrencies(): Array<String>
    // Курсы криптовалют
    fun getCoinRates(toSymbol: String): Flowable<List<CoinRateDomainModel>>
}