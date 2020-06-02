package com.arturdevmob.cryptorates.di.application

import com.arturdevmob.cryptorates.data.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.sources.db.AppDatabase
import com.arturdevmob.cryptorates.data.sources.network.CryptoServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideCoinRepository(network: CryptoServices, database: AppDatabase): CoinRepository {
        return CoinRepository(network, database)
    }
}