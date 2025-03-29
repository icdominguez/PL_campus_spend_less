package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.model.toUser
import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository,
) {
    suspend operator fun invoke(username: String, pin: Int) =
        localSpendLessRepository.getUser(username, pin)?.toUser()
}