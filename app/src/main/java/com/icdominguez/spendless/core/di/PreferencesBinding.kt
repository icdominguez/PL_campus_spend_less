package com.icdominguez.spendless.core.di

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import com.icdominguez.spendless.domain.datasource.SpendLessSharedPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesBinding {
    @Binds
    abstract fun bindSpendLessPreferencesDataSource(
        spendLessSharedPreferencesDataSourceImpl: SpendLessSharedPreferencesDataSourceImpl
    ): SpendLessSharedPreferencesDataSource
}