package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.inquize.domain.repository.IPreferenceRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository

class SignOffUseCase(
    private val userRepository: IUserRepository,
    private val preferenceRepository: IPreferenceRepository
): BrownieUseCase<Unit>() {
    override suspend fun onExecuted() {
        userRepository.closeSession()
        preferenceRepository.clearData()
    }
}