package com.arturdevmob.cryptorates.utils

import androidx.test.rule.ActivityTestRule
import com.arturdevmob.cryptorates.App
import com.arturdevmob.cryptorates.data.repositories.test.ResourceCoinType
import com.arturdevmob.cryptorates.di.application.AppModule
import com.arturdevmob.cryptorates.di.application.test.DaggerTestAppComponent
import com.arturdevmob.cryptorates.di.application.test.TestDataModule
import com.arturdevmob.cryptorates.presentation.SingleActivity

class DaggerActivityTestRule(private val resourceCoinType: ResourceCoinType) :
    ActivityTestRule<SingleActivity>(SingleActivity::class.java) {
    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()

        val context = App.appComponent.getContext()

        // Меняем AppComponent на TestAppComponent
        App.appComponent = DaggerTestAppComponent.builder()
            .testDataModule(TestDataModule(resourceCoinType))
            .appModule(AppModule(context))
            .build()
    }
}