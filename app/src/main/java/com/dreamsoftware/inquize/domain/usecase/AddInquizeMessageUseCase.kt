package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.inquize.domain.model.AddInquizeMessageBO
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository

/**
 * Use case for adding a new question to an existing Inquize conversation.
 *
 * This use case is responsible for:
 * 1. Retrieving the currently authenticated user's ID.
 * 2. Fetching an existing Inquize conversation based on the provided Inquize ID.
 * 3. Resolving the answer to the new question using a multi-modal language model.
 * 4. Adding the new question and its resolved answer to the Inquize conversation history.
 *
 * The use case interacts with the following repositories:
 * - `userRepository`: To retrieve the authenticated user's ID.
 * - `inquizeRepository`: To fetch the Inquize conversation by its ID and to add the new message to the conversation.
 * - `multiModalLanguageModelRepository`: To resolve the question using a multi-modal language model.
 */
class AddInquizeMessageUseCase(
    private val userRepository: IUserRepository,
    private val inquizeRepository: IInquizeRepository,
    private val multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
) : BrownieUseCaseWithParams<AddInquizeMessageUseCase.Params, InquizeBO>() {

    /**
     * Executes the use case to add a new question to an existing Inquize conversation.
     *
     * @param params The parameters required for this use case, including the Inquize ID and the new question.
     * @return The updated Inquize business object containing the conversation history and the new message.
     * @throws Exception If any error occurs during the execution of the use case.
     */
    override suspend fun onExecuted(params: Params): InquizeBO = with(params) {
        // Retrieve the authenticated user's ID
        val userId = userRepository.getUserAuthenticatedUid()

        // Fetch the existing Inquize conversation by ID
        val inquize = inquizeRepository.fetchById(userId = userId, id = inquizeId)

        // Resolve the answer to the new question using the multi-modal language model
        val answer = multiModalLanguageModelRepository.resolveQuestion(ResolveQuestionBO(
            context = inquize.imageDescription,
            question = question,
            history = inquize.messages.map { it.role.name to it.text }
        ))

        // Create a new Inquize message containing the question and the resolved answer
        val newInquizeMessage = AddInquizeMessageBO(
            uid = inquizeId,
            userId = userId,
            question = question,
            answer = answer
        )

        // Add the new message to the Inquize conversation
        inquizeRepository.addMessage(newInquizeMessage)
    }

    /**
     * Data class representing the parameters required to execute the AddInquizeQuestionUseCase.
     *
     * @property inquizeId The ID of the Inquize conversation to which the new question will be added.
     * @property question The new question to be added to the conversation.
     */
    data class Params(
        val inquizeId: String,
        val question: String
    )
}