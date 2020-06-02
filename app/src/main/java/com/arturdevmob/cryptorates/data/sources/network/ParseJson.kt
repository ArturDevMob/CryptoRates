package com.arturdevmob.cryptorates.data.sources.network

import com.arturdevmob.cryptorates.data.sources.db.CoinEntity
import com.google.gson.JsonObject

class ParseJson {
    companion object {
        private const val RAW = "RAW"
        private const val DATA = "Data"
        private const val COIN_INFO = "CoinInfo"
        private const val NAME = "Name"
        // Поля валюты
        private const val FROM_SYMBOL = "FROMSYMBOL"
        private const val TO_SYMBOL = "TOSYMBOL"
        private const val CURRENT_PRICE = "PRICE"
        private const val CHANGE_HOUR = "CHANGEHOUR"
        private const val CHANGE_24HOUR = "CHANGE24HOUR"
        private const val IMAGE_URL = "IMAGEURL"
        // Other
        private const val IMAGE_HOST = "https://www.cryptocompare.com"

        // Принимает ответ сервера в виде JsonObject
        // Распаршивает и возвращает строку с именами криптовалют через запятую
        fun toNameCoins(json: JsonObject): String {
            val nameCoins = mutableListOf<String>()
            val data = json.getAsJsonArray(DATA)

            for (item in data) {
                nameCoins.add(
                    item.asJsonObject
                        .getAsJsonObject(COIN_INFO)
                        .get(NAME)
                        .asString
                )
            }

            return nameCoins.joinToString(",")
        }

        // Принимает ответ сервера в виде JsonObject
        // Распаршивает и возвращает мутабельный лист с entity криптовалют
        fun toCoinEntities(json: JsonObject): MutableList<CoinEntity> {
            val coinEntities = mutableListOf<CoinEntity>()
            val raw = json.getAsJsonObject(RAW)

            for (coinName in raw.keySet()) {
                val coin = raw.getAsJsonObject(coinName)
                val coinInfo = coin.getAsJsonObject(coin.keySet().first())

                coinEntities.add(
                    CoinEntity(
                        coinInfo.get(FROM_SYMBOL).asString,
                        coinInfo.get(TO_SYMBOL).asString,
                        coinInfo.get(CURRENT_PRICE).asDouble,
                        coinInfo.get(CHANGE_HOUR).asDouble,
                        coinInfo.get(CHANGE_24HOUR).asDouble,
                        "$IMAGE_HOST${coinInfo.get(IMAGE_URL).asString}"
                    )
                )
            }

            return coinEntities
        }
    }
}