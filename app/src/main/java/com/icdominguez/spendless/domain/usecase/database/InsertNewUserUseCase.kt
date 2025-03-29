package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.model.UserEntity
import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import com.icdominguez.spendless.presentation.model.User
import javax.inject.Inject

class InsertNewUserUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository
) {
    suspend operator fun invoke(user: User) {
        localSpendLessRepository.insertUser(
            UserEntity(
                username = user.username,
                pin = user.pin,
                expensesFormat = user.expensesFormat,
                currency = user.currency,
                decimalSeparator = user.decimalSeparator,
                sessionExpiryDuration = user.sessionExpiryDuration,
                lockedOutDuration = user.lockedOutDuration,
                thousandSeparator = user.thousandSeparator,
                isLoggedIn = user.isLoggedIn,
            )
        )
    }
}