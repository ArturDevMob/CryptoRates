package com.arturdevmob.cryptorates.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arturdevmob.cryptorates.R
import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_rate_top_coins.view.*

class RateCoinsAdapter : RecyclerView.Adapter<RateCoinsAdapter.ViewHolder>() {
    var coins = mutableListOf<CoinEntity>()
        set(value) {
            coins.clear()
            coins.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rate_top_coins, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val coin = coins[position]

            with(itemView) {
                position_and_symbol_text.text = "${position + 1}. ${coin.fromSymbol}"
                current_price_text.text = coin.currentPrice.toString()
                to_symbol_text.text = coin.toSymbol

                value_change_hour_text.text = coin.percentChangeHour.toString()
                value_change_24hour_text.text = coin.percentChange24Hour.toString()

                value_change_hour_text.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (coin.priceHasRiseItHour()) R.color.value_change_price_up
                        else R.color.value_change_price_down
                    )
                )
                value_change_24hour_text.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (coin.priceHasRiseIt24Hour()) R.color.value_change_price_up
                        else R.color.value_change_price_down
                    )
                )

                Picasso.get()
                    .load(coin.imageUrl)
                    .into(image)
            }
        }
    }
}