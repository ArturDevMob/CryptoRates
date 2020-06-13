package com.arturdevmob.cryptorates.data.repositories.test

// Требуется для CoinRepositoryTest, чтобы указывать,
// какие данные должен возвращать метод getRateTopCoins(toSymbol: String)
// NETWORK - Нужны данные из сети
// CACHED - Нужны данные из кеша
// EMPTY - Нужен пустой список
enum class ResourceCoinType {
    NETWORK, CACHED, EMPTY
}