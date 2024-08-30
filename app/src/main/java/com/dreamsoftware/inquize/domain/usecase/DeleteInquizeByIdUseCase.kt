package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository

class DeleteInquizeByIdUseCase(
    private val userRepository: IUserRepository,
    private val inquizeRepository: IInquizeRepository
) : BrownieUseCaseWithParams<DeleteInquizeByIdUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params): Unit = with(params) {
        val userId = userRepository.getUserAuthenticatedUid()
        inquizeRepository.deleteById(userId = userId, id = id)
    }

    data class Params(
        val id: String,
    )
}