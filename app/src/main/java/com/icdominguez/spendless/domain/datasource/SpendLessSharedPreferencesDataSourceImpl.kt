package com.icdominguez.spendless.domain.datasource

import android.content.SharedPreferences
import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import javax.inject.Inject

class SpendLessSharedPreferencesDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): SpendLessSharedPreferencesDataSource {

    override fun setUsername(username: String) {
        sharedPreferences.edit().putString(USERNAME_KEY, username).apply()
    }

    override fun getUsername(): String? =
        sharedPreferences.getString(USERNAME_KEY, null)

    override fun setPin(pin: Int) {
        sharedPreferences.edit().putInt(PIN_KEY, pin).apply()
    }

    override fun getPin(): Int =
        sharedPreferences.getInt(PIN_KEY, 0)

    override fun setTimeAppClosed(timeAppClosed: Long) {
        sharedPreferences.edit().putLong(TIME_APP_CLOSED_KEY, timeAppClosed).apply()
    }

    override fun getTimeAppClosed(): Long =
        sharedPreferences.getLong(TIME_APP_CLOSED_KEY, 0L)

    override fun removeTimeAppClosed() {
        sharedPreferences.edit().remove(TIME_APP_CLOSED_KEY).apply()
    }

    override fun setSessionCountDownStarted(sessionTimeStartedTimestamp: Long) {
        sharedPreferences.edit().putLong(SESSION_COUNT_DOWN_STARTED_KEY, sessionTimeStartedTimestamp).apply()
    }

    override fun getSessionCountDownStarted(): Long =
        sharedPreferences.getLong(SESSION_COUNT_DOWN_STARTED_KEY, 0L)

    companion object {
        private const val USERNAME_KEY = "username"
        private const val PIN_KEY = "pin"
        private const val TIME_APP_CLOSED_KEY = "time_app_closed"
        private const val SESSION_COUNT_DOWN_STARTED_KEY = "session_count_down_started"
    }

}