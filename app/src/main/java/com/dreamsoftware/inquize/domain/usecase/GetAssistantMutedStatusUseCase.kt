package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.inquize.domain.repository.IPreferenceRepository

class GetAssistantMutedStatusUseCase(
    private val preferencesRepository: IPreferenceRepository
) : BrownieUseCase<Boolean>() {

    override suspend fun onExecuted(): Boolean =
        preferencesRepository.isAssistantMuted()
}