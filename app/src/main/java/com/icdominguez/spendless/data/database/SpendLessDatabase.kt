package com.icdominguez.spendless.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.icdominguez.spendless.data.dao.TransactionDao
import com.icdominguez.spendless.data.dao.UserDao
import com.icdominguez.spendless.data.model.TransactionEntity
import com.icdominguez.spendless.data.model.UserEntity

@Database(entities = [UserEntity::class, TransactionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class SpendLessDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
}