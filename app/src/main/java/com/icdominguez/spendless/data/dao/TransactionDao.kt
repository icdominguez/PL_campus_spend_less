package com.icdominguez.spendless.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.icdominguez.spendless.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE username = :username")
    fun getAll(username: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE recurringFrequency IS NOT NULL AND username = :username")
    fun getRecurringTransactions(username: String): List<TransactionEntity>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate AND username = :username")
    fun getTransactionsFromDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        username: String,
    ): List<TransactionEntity>

    @Query("UPDATE transactions SET nextRecurringDate = NULL WHERE transactionId = :transactionId")
    suspend fun updateNextRecurringDateToNull(transactionId: Int)

}