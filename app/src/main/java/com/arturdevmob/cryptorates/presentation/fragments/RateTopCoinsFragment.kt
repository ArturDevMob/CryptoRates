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
import com.arturdevmob.cryptorates.di.ratetopcoins.DaggerRateTopCoinsComponent
import com.arturdevmob.cryptorates.di.ratetopcoins.RateTopCoinsModule
import com.arturdevmob.cryptorates.presentation.adapters.RateCoinsAdapter
import com.arturdevmob.cryptorates.presentation.models.CoinViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rate_top_coins.*
import javax.inject.Inject

class RateTopCoinsFragment : Fragment() {
    @Inject
    lateinit var coinViewModel: CoinViewModel

    @Inject
    lateinit var coinsAdapter: RateCoinsAdapter

    companion object {
        const val TAG = "RateTopCoinsFragment"

        fun newInstance(): RateTopCoinsFragment {
            return RateTopCoinsFragment()
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
        coinViewModel.startLoadRateTopCoins()
        setupUi()
        setObserverForData()
    }

    private fun setupDi() {
        DaggerRateTopCoinsComponent.builder()
            .appComponent(App.appComponent)
            .rateTopCoinsModule(RateTopCoinsModule(this))
            .build()
            .inject(this)
    }

    private fun setupUi() {
        spinnerToSymbols()
        fabRefreshConnection()
        recyclerViewCoins()
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
        val visible = if (show) View.GONE else View.VISIBLE

        error_content_layout.visibility = visible
        no_connection_server_text.visibility = visible
        refresh_connect_fab.visibility = visible
        showMessage(
            if (show) requireContext().resources.getString(R.string.connection_to_server_restore)
            else requireContext().resources.getString(R.string.connection_to_server_interrupted)
        )
    }

    // true - Отобразит progressbar и скрыть recyclerview
    // false - наоборот
    private fun showProgressLoadingData(show: Boolean) {
        coins_recycler_view.visibility = if (show) View.GONE else View.VISIBLE
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    // Установка spinner с выбором валюты
    // в которые можно конвертировать курс криптовалют
    private fun spinnerToSymbols() {
        val data = coinViewModel.toSymbols
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
    private fun fabRefreshConnection() {
        refresh_connect_fab.setOnClickListener {
            coinViewModel.refreshConnection()
        }
    }

    // Установка recyclerview со списком криптовалют
    private fun recyclerViewCoins() {
        coins_recycler_view.layoutManager = LinearLayoutManager(context)
        coins_recycler_view.setHasFixedSize(true)
        coins_recycler_view.adapter = coinsAdapter
    }

    // Подписки на нужные данные от ViewModel
    private fun setObserverForData() {
        // Получение списка криптовалют
        // Установка списка в адаптер для recyclerview
        coinViewModel.coinsResource
            .observe(viewLifecycleOwner, Observer {
                coinsAdapter.coins = it.data
            })

        // Состояние подключение к серверу
        // false - нет подключения, true - подключен к серверу
        // результат передаем в метод для показа/скрытия ошибок подключения к серверу
        coinViewModel.connectedToServer
            .observe(viewLifecycleOwner, Observer {
                showError(it)
            })

        // Состояние списка с криптовалюьтами
        // false - список пуст, true - список не пуст
        // устанавливаем видимость ошибки о пустом списке криптовалют
        coinViewModel.showEmptyCoinsMessage
            .observe(viewLifecycleOwner, Observer {
                coins_empty_text.visibility = if (it) View.VISIBLE else View.GONE
            })

        // Состояние загрузки данных
        // false - в покое, true - в процессе загрузки
        coinViewModel.loadingData
            .observe(viewLifecycleOwner, Observer {
                showProgressLoadingData(it)
            })
    }
}