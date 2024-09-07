package com.dreamsoftware.inquize.ui.screens.home

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.di.HomeErrorMapper
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.usecase.DeleteInquizeByIdUseCase
import com.dreamsoftware.inquize.domain.usecase.GetAllInquizeByUserUseCase
import com.dreamsoftware.inquize.domain.usecase.SearchInquizeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllInquizeByUserUseCase: GetAllInquizeByUserUseCase,
    private val deleteInquizeByIdUseCase: DeleteInquizeByIdUseCase,
    private val searchInquizeUseCase: SearchInquizeUseCase,
    @HomeErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<HomeUiState, HomeSideEffects>(), HomeScreenActionListener {

    fun loadData() {
        onLoadData()
    }

    override fun onGetDefaultState(): HomeUiState = HomeUiState()

    override fun onInquizeClicked(inquizeBO: InquizeBO) {
        launchSideEffect(HomeSideEffects.OpenInquizeChat(inquizeBO.uid))
    }

    override fun onInquizeDetailClicked(inquizeBO: InquizeBO) {
        launchSideEffect(HomeSideEffects.OpenInquizeDetail(inquizeBO.uid))
    }

    override fun onSearchQueryUpdated(newSearchQuery: String) {
        updateState { it.copy(searchQuery = newSearchQuery) }
        onLoadData()
    }

    override fun onInquizeDeleted(inquizeBO: InquizeBO) {
        updateState { it.copy(confirmDeleteInquize = inquizeBO) }
    }

    override fun onDeleteInquizeConfirmed() {
        doOnUiState {
            confirmDeleteInquize?.let { inquize ->
                executeUseCaseWithParams(
                    useCase = deleteInquizeByIdUseCase,
                    params = DeleteInquizeByIdUseCase.Params(
                        id = inquize.uid
                    ),
                    onSuccess = {
                        onDeleteInquizeCompleted(inquize)
                    },
                    onMapExceptionToState = ::onMapExceptionToState
                )
            }
            updateState { it.copy(confirmDeleteInquize = null) }
        }
    }

    override fun onDeleteInquizeCancelled() {
        updateState { it.copy(confirmDeleteInquize = null) }
    }

    override fun onInfoMessageCleared() {
        updateState { it.copy(infoMessage = null) }
    }

    private fun onDeleteInquizeCompleted(inquize: InquizeBO) {
        updateState { it.copy(inquizeList = it.inquizeList.filter { iq -> iq.uid != inquize.uid }) }
    }

    private fun onLoadInquizeCompleted(data: List<InquizeBO>) {
        updateState {
            it.copy(inquizeList = data)
        }
    }

    private fun onLoadData() {
        doOnUiState {
            if(searchQuery.isEmpty()) {
                executeUseCase(
                    useCase = getAllInquizeByUserUseCase,
                    onSuccess = ::onLoadInquizeCompleted,
                    onMapExceptionToState = ::onMapExceptionToState
                )
            } else {
                executeUseCaseWithParams(
                    useCase = searchInquizeUseCase,
                    params = SearchInquizeUseCase.Params(term = searchQuery),
                    onSuccess = ::onLoadInquizeCompleted,
                    onMapExceptionToState = ::onMapExceptionToState
                )
            }
        }
    }

    private fun onMapExceptionToState(ex: Exception, uiState: HomeUiState) =
        uiState.copy(
            isLoading = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )
}

data class HomeUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val confirmDeleteInquize: InquizeBO? = null,
    val infoMessage: String? = null,
    val inquizeList: List<InquizeBO> = emptyList(),
    val searchQuery: String = String.EMPTY
): UiState<HomeUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): HomeUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}


sealed interface HomeSideEffects: SideEffect {
    data class OpenInquizeDetail(val id: String): HomeSideEffects
    data class OpenInquizeChat(val id: String): HomeSideEffects
}