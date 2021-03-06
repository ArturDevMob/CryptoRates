package com.arturdevmob.cryptorates.di.application

import com.arturdevmob.cryptorates.data.repositories.CoinRepositoryImpl
import com.arturdevmob.cryptorates.data.sources.db.AppDatabase
import com.arturdevmob.cryptorates.data.sources.network.CryptoServices
import com.arturdevmob.cryptorates.domain.repositories.CoinRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideCoinRepository(network: CryptoServices, database: AppDatabase): CoinRepository {
        return CoinRepositoryImpl(network, database)
    }
}