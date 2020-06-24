package com.arturdevmob.cryptorates.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arturdevmob.cryptorates.R
import com.arturdevmob.cryptorates.presentation.fragments.CoinRatesFragment

class SingleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_frame)

        if (fragment == null) {
            fragment = CoinRatesFragment.newInstance()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_frame, fragment, CoinRatesFragment.TAG)
                .commit()
        }
    }
}
