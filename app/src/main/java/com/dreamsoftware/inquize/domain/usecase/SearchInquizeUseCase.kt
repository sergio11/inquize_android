package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository

class SearchInquizeUseCase(
    private val userRepository: IUserRepository,
    private val inquizeRepository: IInquizeRepository,
) : BrownieUseCaseWithParams<SearchInquizeUseCase.Params, List<InquizeBO>>() {

    override suspend fun onExecuted(params: Params): List<InquizeBO> = with(params) {
        val userUid = userRepository.getUserAuthenticatedUid()
        inquizeRepository.search(userUid, term)
    }

    data class Params(
        val term: String
    )
}