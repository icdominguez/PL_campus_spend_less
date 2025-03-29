package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import javax.inject.Inject

class UpdateSessionExpiryAndLockedOutDurationUseCase @Inject constructor(
    private val spendLessRepository: LocalSpendLessRepository
) {
    suspend operator fun invoke(
        username: String,
        sessionExpiryDuration: String,
        lockedOutDuration: String
    ) {
        spendLessRepository.updateSessionExpiryDuration(
            username,
            sessionExpiryDuration,
            lockedOutDuration,
        )
    }
}