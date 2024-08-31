package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO
import com.dreamsoftware.inquize.domain.model.CreateInquizeBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository
import java.util.UUID

class CreateInquizeUseCase(
    private val userRepository: IUserRepository,
    private val inquizeRepository: IInquizeRepository,
    private val multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
) : BrownieUseCaseWithParams<CreateInquizeUseCase.Params, InquizeBO>() {

    override suspend fun onExecuted(params: Params): InquizeBO = with(params) {
        val answer = multiModalLanguageModelRepository.resolveQuestion(toResolveQuestionBO())
        val userId = userRepository.getUserAuthenticatedUid()
        inquizeRepository.create(toSaveInquizeBO(userId = userId, answer = answer))
    }

    private fun Params.toResolveQuestionBO() = ResolveQuestionBO(
        imageUrl = imageUrl,
        question = question
    )

    private fun Params.toSaveInquizeBO(userId: String, answer: String) = CreateInquizeBO(
        uid = UUID.randomUUID().toString(),
        userId = userId,
        imageUrl = imageUrl,
        question = question,
        answer = answer
    )

    data class Params(
        val imageUrl: String,
        val question: String
    )
}