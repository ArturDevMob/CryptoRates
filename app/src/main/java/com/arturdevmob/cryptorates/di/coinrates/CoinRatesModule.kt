package com.arturdevmob.cryptorates.di.coinrates

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider
import com.arturdevmob.cryptorates.di.CoinRatesScope
import com.arturdevmob.cryptorates.domain.repositories.CoinRepository
import com.arturdevmob.cryptorates.domain.usecase.GetCoinRatesUseCase
import com.arturdevmob.cryptorates.domain.usecase.GetCurrencyUseCase
import com.arturdevmob.cryptorates.presentation.adapters.CoinRatesAdapter
import com.arturdevmob.cryptorates.presentation.models.ModelFactory
import com.arturdevmob.cryptorates.presentation.models.CoinRatesViewModel
import dagger.Module
import dagger.Provides

@Module
class CoinRatesModule(private val fragment: Fragment) {
    @Provides
    @CoinRatesScope
    fun provideGetCoinRatesUseCase(coinRepository: CoinRepository): GetCoinRatesUseCase {
        return GetCoinRatesUseCase(coinRepository)
    }

    @Provides
    @CoinRatesScope
    fun provideGetCurrencyUseCase(coinRepository: CoinRepository): GetCurrencyUseCase {
        return GetCurrencyUseCase(coinRepository)
    }

    @Provides
    @CoinRatesScope
    fun provideCoinViewModel(
        schedulerProvider: SchedulerProvider,
        getCoinRatesUseCase: GetCoinRatesUseCase,
        getCurrencyUseCase: GetCurrencyUseCase
    ): CoinRatesViewModel {
        return ViewModelProvider(
            fragment,
            ModelFactory(schedulerProvider, getCoinRatesUseCase, getCurrencyUseCase)
        ).get(CoinRatesViewModel::class.java)
    }

    @Provides
    @CoinRatesScope
    fun provideRateCoinsAdapter(): CoinRatesAdapter {
        return CoinRatesAdapter()
    }
}