package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.inquize.domain.model.AuthUserBO

/**
 * VerifyUserSessionUseCase
 * @param userRepository
 */
class VerifyUserSessionUseCase() : BrownieUseCase<AuthUserBO>() {

    override suspend fun onExecuted(): AuthUserBO {
        throw IllegalStateException()
    }
}