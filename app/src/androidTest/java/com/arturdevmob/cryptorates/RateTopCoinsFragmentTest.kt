package com.arturdevmob.cryptorates

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arturdevmob.cryptorates.data.repositories.test.ResourceCoinType
import com.arturdevmob.cryptorates.utils.DaggerActivityTestRule
import kotlinx.android.synthetic.main.item_rate_top_coins.view.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RateTopCoinsFragmentTest {
    @get:Rule
    var activityRule = DaggerActivityTestRule(ResourceCoinType.NETWORK)

    // Есть подключение к серверу и получены актуальные курсы
    @Test
    fun connectionToNetworkThereAreShowActualCoins() {
        onView(withId(R.id.tosymbol_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.error_content_layout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.no_connection_server_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.coins_empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.coins_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.refresh_connect_fab)).check(matches(not(isDisplayed())))
    }

    // НЕТ подключения к серверу, но получены закэшированные курсы из базы
    @Test
    fun noNetworkConnectionShowCachedCoins() {
        restartActivityWithResourceCoinType(ResourceCoinType.CACHED)

        onView(withId(R.id.tosymbol_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.no_connection_server_text)).check(matches(isDisplayed()))
        onView(withId(R.id.coins_empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.coins_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.refresh_connect_fab)).check(matches(isDisplayed()))
    }

    // НЕТ подключения к серверу и нет курсов для отображения (кэш пустой)
    @Test
    fun noNetworkConnectionAndNoCachedCoins() {
        restartActivityWithResourceCoinType(ResourceCoinType.EMPTY)

        onView(withId(R.id.tosymbol_spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.no_connection_server_text)).check(matches(isDisplayed()))
        onView(withId(R.id.coins_empty_text)).check(matches(isDisplayed()))
        onView(withId(R.id.coins_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.refresh_connect_fab)).check(matches(isDisplayed()))
    }

    // Смена валюты для конвертации курсов
    @Test
    fun changeOfCurrency() {
        val spinner = onView(withId(R.id.tosymbol_spinner))
        val selectedDefaultToSymbol = "USD"
        val selectedToSymbol = "PLN"

        spinner.check(matches(withSpinnerText(selectedDefaultToSymbol)))
        spinner.perform(click())

        onData(equalTo(selectedToSymbol)).perform(click())
        spinner.check(matches(withSpinnerText(selectedToSymbol)))

        onView(withId(R.id.coins_recycler_view)).check(matches(equalsToSymbol(selectedToSymbol)))
    }

    // Перезапуск активити с новым правилом
    private fun restartActivityWithResourceCoinType(resourceCoinType: ResourceCoinType) {
        activityRule.activity.finish()
        activityRule = DaggerActivityTestRule(resourceCoinType)
        activityRule.launchActivity(null)
    }

    private fun equalsToSymbol(toSymbol: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("Сравнение переданной валюты с валютой из элемента в RecyclerView")
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item is RecyclerView) {
                    item.findViewHolderForLayoutPosition(0)?.itemView?.let {
                        return it.to_symbol_text.text == toSymbol
                    }
                }

                return false
            }
        }
    }
}