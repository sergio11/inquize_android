package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository

class GetInquizeByIdUseCase(
    private val userRepository: IUserRepository,
    private val inquizeRepository: IInquizeRepository
) : BrownieUseCaseWithParams<GetInquizeByIdUseCase.Params, InquizeBO>() {

    override suspend fun onExecuted(params: Params): InquizeBO {
        val userId = userRepository.getUserAuthenticatedUid()
        return inquizeRepository.fetchById(userId = userId, id = params.id)
    }

    data class Params(val id: String)
}