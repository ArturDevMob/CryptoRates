package com.arturdevmob.cryptorates.di.ratetopcoins

import com.arturdevmob.cryptorates.di.RateTopCoins
import com.arturdevmob.cryptorates.di.application.AppComponent
import com.arturdevmob.cryptorates.presentation.fragments.RateTopCoinsFragment
import dagger.Component

@Component(dependencies = [AppComponent::class], modules = [RateTopCoinsModule::class])
@RateTopCoins
interface RateTopCoinsComponent {
    fun inject(obj: RateTopCoinsFragment)
}