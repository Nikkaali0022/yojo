package com.yojo.app.data.model

data class VirtualGift(
    val giftId: String = "",
    val giftName: String = "",
    val coinPrice: Long = 0,
    val iconUrl: String = "",
    val animationAsset: String = ""
)

data class WalletTransaction(
    val transactionId: String = "",
    val userId: String = "",
    val type: String = "", // "credit" or "debit"
    val amount: Long = 0,
    val currencyType: String = "coins", // "coins" or "diamonds"
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
