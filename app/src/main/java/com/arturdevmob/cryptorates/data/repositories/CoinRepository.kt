package com.arturdevmob.cryptorates.data.repositories

import com.arturdevmob.cryptorates.data.Resource
import com.arturdevmob.cryptorates.data.StatusNetworkDataLoad
import com.arturdevmob.cryptorates.data.sources.db.AppDatabase
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import com.arturdevmob.cryptorates.data.sources.network.CryptoServices
import com.arturdevmob.cryptorates.data.sources.network.ParseJson
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinRepository(private val network: CryptoServices, private val database: AppDatabase) {
    fun getToSymbols(): Array<String> {
        return arrayOf(
            "USD",
            "RUB",
            "EUR",
            "CHF",
            "GBP",
            "JPY",
            "UAH",
            "KZT",
            "BYN",
            "TRY",
            "CNY",
            "AUD",
            "CAD",
            "PLN"
        )
    }

    fun getRateTopCoins(toSymbol: String): Flowable<Resource<MutableList<CoinEntity>>> {
        return Flowable.interval(0, 10, TimeUnit.SECONDS)
            .flatMap { network.getNameTopCoins(toSymbol = toSymbol) }
            .map { ParseJson.toNameCoins(it) }
            .flatMap { network.getCoins(it, toSymbol) }
            .map {
                val coinEntities = ParseJson.toCoinEntities(it)
                database.coinDao().removeAll()
                database.coinDao().insert(coinEntities)
                Resource(StatusNetworkDataLoad.SUCCESS, coinEntities)
            }
            .onErrorReturn {
                val coinEntities = database.coinDao().getAllCoins()
                Resource(StatusNetworkDataLoad.ERROR, coinEntities)
            }
            .subscribeOn(Schedulers.io())
    }

    fun getSelectedToSymbolDefault(): String {
        return getToSymbols()[0]
    }
}