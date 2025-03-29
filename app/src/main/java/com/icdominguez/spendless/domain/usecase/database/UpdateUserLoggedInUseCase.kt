package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import javax.inject.Inject

class UpdateUserLoggedInUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository
) {
    suspend operator fun invoke(
        username: String,
        isLoggedIn: Boolean
    ) = localSpendLessRepository.updateUserIsLoggedIn(
        username = username,
        isLoggedIn = isLoggedIn
    )
}