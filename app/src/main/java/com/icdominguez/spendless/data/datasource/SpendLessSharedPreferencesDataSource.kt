package com.icdominguez.spendless.data.datasource

interface SpendLessSharedPreferencesDataSource {
    fun setUsername(username: String)
    fun getUsername(): String?
    fun setPin(pin: Int)
    fun getPin(): Int
    fun setTimeAppClosed(timeAppClosed: Long)
    fun getTimeAppClosed(): Long
    fun removeTimeAppClosed()
    fun setSessionCountDownStarted(sessionTimeStartedTimestamp: Long)
    fun getSessionCountDownStarted(): Long
}