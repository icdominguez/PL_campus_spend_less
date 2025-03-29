package com.icdominguez.spendless.domain.di

import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import com.icdominguez.spendless.domain.repository.LocalSpendLessRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {
    @Binds
    fun bindLocalSpendLessRepository(
        localSpendLessRepositoryImpl: LocalSpendLessRepositoryImpl
    ): LocalSpendLessRepository
}