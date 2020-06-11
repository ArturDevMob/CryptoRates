package com.arturdevmob.cryptorates.data.repositories

import com.arturdevmob.cryptorates.business.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.Resource
import com.arturdevmob.cryptorates.data.StatusNetwork
import com.arturdevmob.cryptorates.data.sources.db.AppDatabase
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import com.arturdevmob.cryptorates.data.sources.network.CryptoServices
import com.arturdevmob.cryptorates.data.sources.network.ParseJson
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class CoinRepositoryImpl(
    private val network: CryptoServices,
    private val database: AppDatabase
) : CoinRepository {
    override fun getToSymbols(): Array<String> {
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

    override fun getRateTopCoins(toSymbol: String): Flowable<Resource<MutableList<CoinEntity>>> {
        return Flowable.interval(0, 10, TimeUnit.SECONDS)
            .flatMap { network.getNameTopCoins(toSymbol = toSymbol) }
            .map { ParseJson.toNameCoins(it) }
            .flatMap { network.getCoins(it, toSymbol) }
            .map {
                val coinEntities = ParseJson.toCoinEntities(it)
                database.coinDao().removeAll()
                database.coinDao().insert(coinEntities)
                Resource(StatusNetwork.SUCCESS, coinEntities)
            }
            .onErrorReturn {
                val coinEntities = database.coinDao().getAllCoins()
                Resource(StatusNetwork.ERROR, coinEntities)
            }
    }

    override fun getSelectedToSymbolDefault(): String {
        return getToSymbols()[0]
    }
}