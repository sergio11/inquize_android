package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository

class GetAllInquizeByUserUseCase(
    private val userRepository: IUserRepository,
    private val inquizeRepository: IInquizeRepository
) : BrownieUseCase<List<InquizeBO>>() {

    override suspend fun onExecuted(): List<InquizeBO> =
        inquizeRepository.fetchAllByUserId(userId = userRepository.getUserAuthenticatedUid())
}