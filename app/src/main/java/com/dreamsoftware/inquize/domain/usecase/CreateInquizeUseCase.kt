package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO
import com.dreamsoftware.inquize.domain.model.CreateInquizeBO
import com.dreamsoftware.inquize.domain.repository.IImageRepository
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository
import java.util.UUID

/**
 * Use case for creating a new Inquize entry.
 * This involves saving an image, generating an answer using a multi-modal language model,
 * and then creating an Inquize record with the generated data.
 *
 * @param userRepository Repository for user-related operations.
 * @param imageRepository Repository for image-related operations.
 * @param inquizeRepository Repository for Inquize records.
 * @param multiModalLanguageModelRepository Repository for multi-modal language model interactions.
 */
class CreateInquizeUseCase(
    private val userRepository: IUserRepository,
    private val imageRepository: IImageRepository,
    private val inquizeRepository: IInquizeRepository,
    private val multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
) : BrownieUseCaseWithParams<CreateInquizeUseCase.Params, InquizeBO>() {

    /**
     * Executes the use case to create a new Inquize record.
     *
     * @param params Parameters containing the image URL and the user's question.
     * @return The newly created Inquize business object (InquizeBO).
     */
    override suspend fun onExecuted(params: Params): InquizeBO = with(params) {
        // Generate a unique ID for the Inquize entry
        val inquizeId = UUID.randomUUID().toString()

        // Save the image and get the new image URL
        val newImageUrl = imageRepository.save(path = imageUrl, name = inquizeId)

        // Generate a description for the image
        val imageDescription = multiModalLanguageModelRepository.generateImageDescription(newImageUrl)

        // Prepare the question for resolution
        val resolveQuestion = ResolveQuestionBO(
            context = imageDescription,
            question = question
        )

        // Resolve the question
        val answer = multiModalLanguageModelRepository.resolveQuestion(resolveQuestion)

        // Get the authenticated user's ID
        val userId = userRepository.getUserAuthenticatedUid()

        // Create the Inquize entry
        val inquizeBO = CreateInquizeBO(
            uid = inquizeId,
            userId = userId,
            imageUrl = newImageUrl,
            imageDescription = imageDescription,
            question = question,
            answer = answer
        )

        // Save the Inquize entry
        inquizeRepository.create(inquizeBO)
    }

    /**
     * Data class representing the parameters for the use case.
     *
     * @property imageUrl The URL of the image to be processed.
     * @property question The question asked by the user.
     */
    data class Params(
        val imageUrl: String,
        val question: String
    )
}