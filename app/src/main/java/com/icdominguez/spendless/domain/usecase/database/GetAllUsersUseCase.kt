package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.domain.repository.LocalSpendLessRepositoryImpl
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val localSpendLessRepositoryImpl: LocalSpendLessRepositoryImpl
) {
    suspend operator fun invoke() =
        localSpendLessRepositoryImpl.getAllUsers()
}