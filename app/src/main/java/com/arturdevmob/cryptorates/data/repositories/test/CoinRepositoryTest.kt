package com.arturdevmob.cryptorates.data.repositories.test

import com.arturdevmob.cryptorates.business.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.Resource
import com.arturdevmob.cryptorates.data.StatusNetwork
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

// Реализация репозитория для UI тестирования
// resourceCoinType указывает, какие данные должен вернуть метод getRateTopCoins
class CoinRepositoryTest(private var resourceCoinType: ResourceCoinType) : CoinRepository {
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
        val resource = when (resourceCoinType) {
            ResourceCoinType.NETWORK -> {
                Resource(StatusNetwork.SUCCESS, createCoinsList(toSymbol))
            }
            ResourceCoinType.CACHED -> {
                Resource(StatusNetwork.ERROR, createCoinsList(toSymbol))
            }
            ResourceCoinType.EMPTY -> {
                Resource(StatusNetwork.ERROR, mutableListOf())
            }
        }

        return Flowable.interval(0, 10, TimeUnit.SECONDS)
            .map { resource }
    }

    override fun getSelectedToSymbolDefault(): String {
        return getToSymbols()[0]
    }

    private fun createCoinsList(toSymbol: String = "USD"): MutableList<CoinEntity> {
        val coins = mutableListOf<CoinEntity>()

        coins.add(
            CoinEntity("BTC", toSymbol, 1.2, 3.4, 5.6, "url")
        )
        coins.add(
            CoinEntity("ETH", toSymbol, 11.22, 33.33, 44.44, "url")
        )

        return coins
    }
}