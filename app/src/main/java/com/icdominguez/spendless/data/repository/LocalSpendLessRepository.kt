package com.icdominguez.spendless.data.repository

import com.icdominguez.spendless.data.model.TransactionEntity
import com.icdominguez.spendless.data.model.UserEntity
import com.icdominguez.spendless.presentation.model.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface LocalSpendLessRepository {
    // region Users
    suspend fun getAllUsers(): List<User>
    suspend fun getUserLoggedIn(): Flow<UserEntity?>
    suspend fun insertUser(user: UserEntity)
    suspend fun getUser(username: String, pin: Int): UserEntity?
    suspend fun updateUserIsLoggedIn(username: String, isLoggedIn: Boolean)
    suspend fun updateUserPreferences(
        username: String,
        expenseFormat: String,
        currencySymbol: String,
        decimalSeparator: String,
        thousandSeparator: String,
    )
    // endregion
    // region Transaction
    suspend fun getAllTransactions(username: String): Flow<List<TransactionEntity>>
    suspend fun insertTransaction(transaction: TransactionEntity)
    suspend fun updateSessionExpiryDuration(
        username: String,
        sessionExpiryDuration: String,
        lockedOutDuration: String
    )
    suspend fun getTransactionsFromDate(
        username: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<TransactionEntity>
    suspend fun getRecurringTransactions(username: String): List<TransactionEntity>
    suspend fun updateNextRecurringDateToNull(transactionId: Int)
    // endregion
}