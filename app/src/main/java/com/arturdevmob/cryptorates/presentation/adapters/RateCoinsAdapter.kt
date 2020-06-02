package com.arturdevmob.cryptorates.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                current_price_text.text = "${coin.currentPrice} ${coin.toSymbol}"

                // Заглушка, переделать!
                val rand1 = (Math.random() * 5).toInt()
                val rand2 = (Math.random() * 5).toInt()
                val rand12 = (Math.random() * 99).toInt()
                val rand22 = (Math.random() * 99).toInt()
                value_change_hour_text.text = "${rand1}.${rand12}0%"
                value_change_24hour_text.text = "-${rand2}.${rand22}0%"
                value_change_hour_text.setTextColor(resources.getColor(R.color.value_change_price_up))
                value_change_24hour_text.setTextColor(resources.getColor(R.color.value_change_price_down))

                Picasso.get()
                    .load(coin.imageUrl)
                    .into(image)
            }
        }
    }
}