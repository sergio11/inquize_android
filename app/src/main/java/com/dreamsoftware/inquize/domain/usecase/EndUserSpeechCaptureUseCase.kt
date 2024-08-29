package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.inquize.domain.service.ITranscriptionService

class EndUserSpeechCaptureUseCase(
    private val transcriptionService: ITranscriptionService
) : BrownieUseCase<Unit>() {

    override suspend fun onExecuted() =
        transcriptionService.stopListening()
}