package com.arturdevmob.cryptorates.business.repositories

import com.arturdevmob.cryptorates.data.Resource
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import io.reactivex.Flowable

interface CoinRepository {
    fun getToSymbols(): Array<String>
    fun getRateTopCoins(toSymbol: String): Flowable<Resource<MutableList<CoinEntity>>>
    fun getSelectedToSymbolDefault(): String
}