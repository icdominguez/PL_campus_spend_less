package com.icdominguez.spendless.data.di

import android.content.Context
import androidx.room.Room
import com.icdominguez.spendless.core.Commons.DATABASE_NAME
import com.icdominguez.spendless.data.dao.TransactionDao
import com.icdominguez.spendless.data.dao.UserDao
import com.icdominguez.spendless.data.database.SpendLessDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        passphrase: ByteArray
    ): SpendLessDatabase {
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context.applicationContext,
            SpendLessDatabase::class.java,
            DATABASE_NAME
        )
        .openHelperFactory(factory)
        .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: SpendLessDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideTransactionDao(database: SpendLessDatabase): TransactionDao {
        return database.transactionDao()
    }
}