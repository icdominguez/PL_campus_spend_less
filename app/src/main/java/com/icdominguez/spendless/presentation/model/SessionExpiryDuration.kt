package com.icdominguez.spendless.presentation.model

enum class SessionExpiryDuration(val value: String, val millis: Long) {
    MINUTES_5(value = "5 min", millis = 10000L),
    MINUTES_15(value = "15 min", millis = 1500000L),
    MINUTES_30(value = "30 min", millis = 3000000L),
    HOURS_1(value = "1 hour", millis = 6000000L);
}