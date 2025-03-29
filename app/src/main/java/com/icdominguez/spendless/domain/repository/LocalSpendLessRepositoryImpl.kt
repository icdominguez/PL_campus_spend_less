package com.icdominguez.spendless.domain.repository

import com.icdominguez.spendless.data.dao.TransactionDao
import com.icdominguez.spendless.data.dao.UserDao
import com.icdominguez.spendless.data.model.TransactionEntity
import com.icdominguez.spendless.data.model.UserEntity
import com.icdominguez.spendless.data.model.toUser
import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import com.icdominguez.spendless.presentation.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class LocalSpendLessRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val transactionDao: TransactionDao,
) : LocalSpendLessRepository {

    // region Users
    override suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            userDao.getAll().map { it.toUser() }
        }
    }

    override suspend fun getUserLoggedIn(): Flow<UserEntity?> = flow {
        userDao.getUserLoggedIn().collect {
            emit(it)
        }
    }

    override suspend fun insertUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    override suspend fun getUser(username: String, pin: Int): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUser(username, pin)
        }
    }

    override suspend fun updateUserIsLoggedIn(username: String, isLoggedIn: Boolean) {
        return withContext(Dispatchers.IO) {
            userDao.updateUserIsLoggedIn(username, isLoggedIn)
        }
    }

    override suspend fun updateUserPreferences(
        username: String,
        expenseFormat: String,
        currencySymbol: String,
        decimalSeparator: String,
        thousandSeparator: String
    ) {
        return withContext(Dispatchers.IO) {
            userDao.updatePreferences(
                username = username,
                expenseFormat = expenseFormat,
                currencySymbol = currencySymbol,
                decimalSeparator = decimalSeparator,
                thousandSeparator = thousandSeparator
            )
        }
    }

    // endregion

    // region Transactions
    override suspend fun getAllTransactions(username: String): Flow<List<TransactionEntity>> = flow {
        transactionDao.getAll(
            username = username
        ).collect {
            emit(it)
        }
    }

    override suspend fun insertTransaction(transaction: TransactionEntity) {
        return withContext(Dispatchers.IO) {
            transactionDao.insertTransaction(transaction)
        }
    }

    override suspend fun updateSessionExpiryDuration(
        username: String,
        sessionExpiryDuration: String,
        lockedOutDuration: String
    ) {
        return withContext(Dispatchers.IO){
            userDao.updateLockedOutDuration(
                username = username,
                sessionExpiryDuration = sessionExpiryDuration,
                lockedOutDuration = lockedOutDuration,
            )
        }
    }

    override suspend fun getTransactionsFromDate(
        username: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<TransactionEntity> {
        return withContext(Dispatchers.IO) {
            transactionDao.getTransactionsFromDate(
                username = username,
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    override suspend fun getRecurringTransactions(username: String): List<TransactionEntity> {
        return withContext(Dispatchers.IO) {
            transactionDao.getRecurringTransactions(username)
        }
    }

    override suspend fun updateNextRecurringDateToNull(transactionId: Int) {
        return withContext(Dispatchers.IO) {
            transactionDao.updateNextRecurringDateToNull(transactionId = transactionId)
        }
    }
    // endregion
}