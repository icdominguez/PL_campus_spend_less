package com.icdominguez.spendless.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.icdominguez.spendless.presentation.model.User
import java.time.LocalDateTime

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val username: String,
    val pin: Int,
    val expensesFormat: String,
    val currency: String,
    val decimalSeparator: String,
    val thousandSeparator: String,
    val sessionExpiryDuration: String,
    val lockedOutDuration: String,
    val lastConnection: LocalDateTime = LocalDateTime.now(),
    val isLoggedIn: Boolean = false,
)

fun UserEntity.toUser(): User {
    return User(
        username = username,
        pin = pin,
        expensesFormat = expensesFormat,
        currency = currency,
        decimalSeparator = decimalSeparator,
        thousandSeparator = thousandSeparator,
        sessionExpiryDuration = sessionExpiryDuration,
        lockedOutDuration = lockedOutDuration,
        lastConnection = lastConnection,
        isLoggedIn = isLoggedIn
    )
}