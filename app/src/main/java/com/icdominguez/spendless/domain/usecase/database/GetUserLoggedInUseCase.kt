package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import javax.inject.Inject

class GetUserLoggedInUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository,
) {
    suspend operator fun invoke() =
        localSpendLessRepository.getAllUsers().firstOrNull { it.isLoggedIn }
}