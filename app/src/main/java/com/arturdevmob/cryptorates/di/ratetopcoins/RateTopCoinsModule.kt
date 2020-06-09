package com.arturdevmob.cryptorates.di.ratetopcoins

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arturdevmob.cryptorates.business.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider
import com.arturdevmob.cryptorates.di.RateTopCoins
import com.arturdevmob.cryptorates.presentation.adapters.RateCoinsAdapter
import com.arturdevmob.cryptorates.presentation.models.CoinModelFactory
import com.arturdevmob.cryptorates.presentation.models.CoinViewModel
import dagger.Module
import dagger.Provides

@Module
class RateTopCoinsModule(private val fragment: Fragment) {
    @Provides
    @RateTopCoins
    fun provideCoinViewModel(
        schedulerProvider: SchedulerProvider,
        coinRepository: CoinRepository
    ): CoinViewModel {
        return ViewModelProvider(
            fragment,
            CoinModelFactory(schedulerProvider, coinRepository)
        ).get(CoinViewModel::class.java)
    }

    @Provides
    @RateTopCoins
    fun provideRateCoinsAdapter(): RateCoinsAdapter {
        return RateCoinsAdapter()
    }
}