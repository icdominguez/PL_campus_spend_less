package com.icdominguez.spendless.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.icdominguez.spendless.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getUserLoggedIn(): Flow<UserEntity?>

    @Insert
    suspend fun insert(vararg userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username AND pin = :pin")
    suspend fun getUser(username: String, pin: Int): UserEntity?

    @Query("UPDATE users SET isLoggedIn = :loggedIn WHERE username = :username")
    suspend fun updateUserIsLoggedIn(username: String, loggedIn: Boolean)

    @Query("UPDATE users SET sessionExpiryDuration = :sessionExpiryDuration WHERE username = :username")
    fun updateSessionExpiryDuration(username: String, sessionExpiryDuration: String)

    @Query("UPDATE users SET sessionExpiryDuration = :sessionExpiryDuration, lockedOutDuration = :lockedOutDuration WHERE username = :username")
    fun updateLockedOutDuration(
        username: String,
        lockedOutDuration: String,
        sessionExpiryDuration: String
    )

    @Query("UPDATE users SET expensesFormat = :expenseFormat, currency = :currencySymbol, decimalSeparator = :decimalSeparator, thousandSeparator = :thousandSeparator WHERE username = :username")
    fun updatePreferences(
        username: String,
        expenseFormat: String,
        currencySymbol: String,
        decimalSeparator: String,
        thousandSeparator: String
    )
}