package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.SaveInquizeBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository
import java.util.UUID

class CreateInquizeUseCase(
    private val userRepository: IUserRepository,
    private val inquizeRepository: IInquizeRepository
) : BrownieUseCaseWithParams<CreateInquizeUseCase.Params, InquizeBO>() {

    override suspend fun onExecuted(params: Params): InquizeBO = with(params) {
        inquizeRepository.save(toSaveInquizeBO(userId = userRepository.getUserAuthenticatedUid()))
    }

    private fun Params.toSaveInquizeBO(userId: String) = SaveInquizeBO(
        uid = UUID.randomUUID().toString(),
        userId = userId,
        imageUrl = imageUrl,
        prompt = prompt
    )

    data class Params(
        val imageUrl: String,
        val prompt: String
    )
}