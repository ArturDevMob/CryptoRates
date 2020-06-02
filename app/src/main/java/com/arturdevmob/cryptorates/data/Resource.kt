package com.arturdevmob.cryptorates.data

// Оборачиваем данные в этот класс
// Чтобы различать, откуда пришли данные
// status = StatusNetworkDataLoad.SUCCESS - соединение с сервером есть, значит данные пришли из сети
// status = StatusNetworkDataLoad.ERROR - соединение с сервером НЕТ, если data не пуст, значит данные пришли из БД
data class Resource<T>(var status: StatusNetworkDataLoad, var data: T)