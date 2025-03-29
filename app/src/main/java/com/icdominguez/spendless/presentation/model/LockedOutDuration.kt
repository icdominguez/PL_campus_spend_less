package com.icdominguez.spendless.presentation.model

enum class LockedOutDuration(
    val value: String,
    val millis: Long,
) {
    SECONDS_15(value = "15s", millis = 15000),
    SECONDS_30(value = "30s", millis = 30000),
    MINUTES_1(value = "1 min", millis = 60000),
    MINUTES_5(value = "5 min", millis = 300000),
}