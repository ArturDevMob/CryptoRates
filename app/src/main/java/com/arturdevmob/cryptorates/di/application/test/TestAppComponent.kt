package com.arturdevmob.cryptorates.di.application.test

import android.content.Context
import com.arturdevmob.cryptorates.di.application.AppComponent
import com.arturdevmob.cryptorates.di.application.AppModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [TestDataModule::class, AppModule::class])
@Singleton
interface TestAppComponent : AppComponent