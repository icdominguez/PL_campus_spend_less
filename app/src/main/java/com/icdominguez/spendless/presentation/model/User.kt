package com.icdominguez.spendless.presentation.model

import java.time.LocalDateTime

data class User(
    val userId: Int = 0,
    val username: String,
    val pin: Int,
    val expensesFormat: String,
    val currency: String,
    val decimalSeparator: String,
    val thousandSeparator: String,
    val lastConnection: LocalDateTime = LocalDateTime.now(),
    val sessionExpiryDuration: String,
    val lockedOutDuration: String,
    val isLoggedIn: Boolean = false,
)
