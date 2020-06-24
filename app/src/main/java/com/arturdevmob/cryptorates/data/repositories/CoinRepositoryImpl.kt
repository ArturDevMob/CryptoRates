package com.arturdevmob.cryptorates.data.repositories

import com.arturdevmob.cryptorates.domain.models.CoinRateDomainModel
import com.arturdevmob.cryptorates.domain.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.sources.db.AppDatabase
import com.arturdevmob.cryptorates.data.sources.network.CryptoServices
import com.arturdevmob.cryptorates.data.sources.network.ParseJson
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class CoinRepositoryImpl(
    private val network: CryptoServices,
    private val database: AppDatabase
) : CoinRepository {
    override fun getCurrencies(): Array<String> {
        return arrayOf(
            "USD", "RUB", "EUR", "CHF", "GBP", "JPY", "UAH",
            "KZT", "BYN", "TRY", "CNY", "AUD", "CAD", "PLN"
        )
    }

    override fun getCoinRates(toSymbol: String): Flowable<List<CoinRateDomainModel>> {
        return Flowable.interval(0, 10, TimeUnit.SECONDS)
            .flatMap { network.getNameTopCoins(toSymbol = toSymbol) }
            .map { ParseJson.toNameCoins(it) }
            .flatMap { network.getCoins(it, toSymbol) }
            .map {
                val coinEntities = ParseJson.toCoinEntities(it)
                database.coinDao().removeAll()
                database.coinDao().insert(coinEntities)
                coinEntities
            }
            .onErrorReturn {
                database.coinDao().getAllCoins()
            }
            .map { list -> list.map { it.toDomainModel() } }
    }
}