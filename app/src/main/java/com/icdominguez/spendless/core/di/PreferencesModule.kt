package com.icdominguez.spendless.core.di

import android.content.Context
import android.content.SharedPreferences
import com.icdominguez.spendless.core.preferences.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    fun provideSpendLessSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = PreferencesHelper.getPrefs(context)

    @Provides
    fun provideEncryptedPassphrase(
        @ApplicationContext context: Context
    ): ByteArray = PreferencesHelper.getDecryptedPassphrase(context)
}