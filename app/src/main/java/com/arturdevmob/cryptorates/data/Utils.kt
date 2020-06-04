package com.arturdevmob.cryptorates.data

import java.text.DecimalFormat
import java.text.NumberFormat

class Utils {
    companion object {
        // Округления до знака после запятой
        // number - число над которым провести операцию, amountNumbers - сколько чисел после запятой оставить
        // 1.02351465 -> 1.02
        fun roundNumberToDecimalPlace(number: Double, amountNumbers: Int): Double {
            return String.format("%.${amountNumbers}f", number).toDouble()
        }

        // Возвращает процент числа X от числа Y
        fun percentXFromY(x: Double, y: Double): Double {
            return (x / y) * 100
        }
    }
}