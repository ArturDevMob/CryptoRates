package com.arturdevmob.cryptorates.di.coinrates

import com.arturdevmob.cryptorates.di.CoinRatesScope
import com.arturdevmob.cryptorates.di.application.AppComponent
import com.arturdevmob.cryptorates.presentation.fragments.CoinRatesFragment
import dagger.Component

@Component(dependencies = [AppComponent::class], modules = [CoinRatesModule::class])
@CoinRatesScope
interface CoinRatesComponent {
    fun inject(obj: CoinRatesFragment)
}