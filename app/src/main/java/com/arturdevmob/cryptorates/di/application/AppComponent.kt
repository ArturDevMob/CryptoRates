package com.arturdevmob.cryptorates.di.application

import android.content.Context
import com.arturdevmob.cryptorates.domain.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.sources.db.AppDatabase
import com.arturdevmob.cryptorates.data.sources.network.CryptoServices
import com.arturdevmob.cryptorates.data.utils.rx.SchedulerProvider
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun getContext(): Context
    fun getCryptoServices(): CryptoServices
    fun getAppDatabase(): AppDatabase
    fun getSchedulerProvider(): SchedulerProvider

    fun getCoinRepository(): CoinRepository
}