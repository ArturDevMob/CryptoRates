package com.arturdevmob.cryptorates.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arturdevmob.cryptorates.App
import com.arturdevmob.cryptorates.R
import com.arturdevmob.cryptorates.di.coinrates.CoinRatesModule
import com.arturdevmob.cryptorates.di.coinrates.DaggerCoinRatesComponent
import com.arturdevmob.cryptorates.presentation.adapters.CoinRatesAdapter
import com.arturdevmob.cryptorates.presentation.models.CoinRatesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rate_top_coins.*
import javax.inject.Inject

class CoinRatesFragment : Fragment() {
    @Inject
    lateinit var coinViewModel: CoinRatesViewModel

    @Inject
    lateinit var coinsAdapter: CoinRatesAdapter

    companion object {
        const val TAG = "RateTopCoinsFragment"

        fun newInstance(): CoinRatesFragment {
            return CoinRatesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rate_top_coins, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDi()
        initViewModel()
        setSpinnerToSymbols()
        setFabRefreshConnection()
        setRecyclerViewCoins()
        setCoinRatesObserver()
    }

    private fun initViewModel() {
        coinViewModel.init()
    }

    private fun setupDi() {
        DaggerCoinRatesComponent.builder()
            .appComponent(App.appComponent)
            .coinRatesModule(CoinRatesModule(this))
            .build()
            .inject(this)
    }

    // Snackbar с уведомлениями
    private fun showMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    // true - Отобразить вьюхи связанные с ошибкой сети
    // false - скрыть
    private fun showError(show: Boolean) {
        val visible = if (show) View.VISIBLE else View.GONE

        if (error_content_layout.visibility != visible) {
            error_content_layout.visibility = visible
            no_connection_server_text.visibility = visible
            refresh_connect_fab.visibility = visible

            showMessage(
                if (show) requireContext().resources.getString(R.string.connection_to_server_interrupted)
                else requireContext().resources.getString(R.string.connection_to_server_restore)
            )
        }
    }

    private fun showEmptyCoinRates(show: Boolean) {
        val visible = if (show) View.VISIBLE else View.GONE

        if (coins_empty_text.visibility != visible) {
            coins_empty_text.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    // true - Отобразит progressbar и скрыть recyclerview
    // false - наоборот
    private fun showProgressLoadingData(show: Boolean) {
        val visible = if (show) View.VISIBLE else View.GONE

        if (progress_bar.visibility != visible) {
            progress_bar.visibility = visible
            coins_recycler_view.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    // Установка spinner с выбором валюты
    // в которые можно конвертировать курс криптовалют
    private fun setSpinnerToSymbols() {
        val data = coinViewModel.toSymbols.value ?: arrayOf()
        val toSymbolAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)

        toSymbolAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)

        tosymbol_spinner.prompt = requireContext().resources.getString(R.string.choose_currency)
        tosymbol_spinner.adapter = toSymbolAdapter
        tosymbol_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                coinViewModel.changeSelectedToSymbol(data[position])
            }
        }
    }

    // Установка слушатель кнопки FAB
    // Перезапускает соединение с сервером
    private fun setFabRefreshConnection() {
        refresh_connect_fab.setOnClickListener {
            val toSymbol = tosymbol_spinner.selectedItem as String
            coinViewModel.refreshConnection(toSymbol)
        }
    }

    // Установка recyclerview со списком криптовалют
    private fun setRecyclerViewCoins() {
        coins_recycler_view.layoutManager = LinearLayoutManager(context)
        coins_recycler_view.setHasFixedSize(true)
        coins_recycler_view.adapter = coinsAdapter
    }

    private fun setCoinRatesObserver() {
        coinViewModel.coinRates.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CoinRatesViewModel.CoinRates.Loading -> {
                    showProgressLoadingData(true)
                }
                is CoinRatesViewModel.CoinRates.FromNetwork -> {
                    showProgressLoadingData(false)
                    showError(false)
                    showEmptyCoinRates(false)
                    coinsAdapter.coins = it.data.toMutableList()
                }
                is CoinRatesViewModel.CoinRates.FromDb -> {
                    showProgressLoadingData(false)
                    showError(true)
                    showEmptyCoinRates(false)
                    coinsAdapter.coins = it.data.toMutableList()
                }
                is CoinRatesViewModel.CoinRates.Empty -> {
                    showProgressLoadingData(false)
                    showError(true)
                    showEmptyCoinRates(true)
                }
            }
        })
    }
}