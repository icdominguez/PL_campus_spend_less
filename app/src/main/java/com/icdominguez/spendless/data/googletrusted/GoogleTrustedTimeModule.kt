package com.icdominguez.spendless.data.googletrusted

import android.content.Context
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleTrustedTimeModule {
    @Provides
    @Singleton
    fun provideTrustedTimeClient(@ApplicationContext context: Context): GoogleTrustedTimeManager {
        return GoogleTrustedTimeManager(context)
    }
}