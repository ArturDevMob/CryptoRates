package com.arturdevmob.cryptorates.di.application.test

import com.arturdevmob.cryptorates.business.repositories.CoinRepository
import com.arturdevmob.cryptorates.data.repositories.test.CoinRepositoryTest
import com.arturdevmob.cryptorates.data.repositories.test.ResourceCoinType
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDataModule(private var resourceCoinType: ResourceCoinType) {
    @Provides
    @Singleton
    fun provideCoinRepository(): CoinRepository {
        return CoinRepositoryTest(resourceCoinType)
    }
}