package com.arturdevmob.cryptorates.domain.usecase

import com.arturdevmob.cryptorates.domain.models.CoinRateDomainModel
import com.arturdevmob.cryptorates.domain.repositories.CoinRepository
import io.reactivex.Flowable

class GetCoinRatesUseCase(private val coinRepository: CoinRepository) {
    fun execute(toSymbol: String): Flowable<List<CoinRateDomainModel>> {
        return coinRepository.getCoinRates(toSymbol)
    }
}