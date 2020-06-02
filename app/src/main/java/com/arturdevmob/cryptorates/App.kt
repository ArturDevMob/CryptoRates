package com.arturdevmob.cryptorates

import android.app.Application
import com.arturdevmob.cryptorates.di.application.AppComponent
import com.arturdevmob.cryptorates.di.application.AppModule
import com.arturdevmob.cryptorates.di.application.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }
}