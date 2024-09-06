package com.dreamsoftware.inquize.ui.screens.detail

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.usecase.DeleteInquizeByIdUseCase
import com.dreamsoftware.inquize.domain.usecase.GetInquizeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InquizeDetailViewModel @Inject constructor(
    private val getInquizeByIdUseCase: GetInquizeByIdUseCase,
    private val deleteInquizeByIdUseCase: DeleteInquizeByIdUseCase,
) : BrownieViewModel<InquizeDetailUiState, InquizeDetailSideEffects>(), InquizeDetailScreenActionListener {

    fun load(id: String) {
        executeUseCaseWithParams(
            useCase = getInquizeByIdUseCase,
            params = GetInquizeByIdUseCase.Params(id = id),
            onSuccess = ::onLoadInquizeDetailCompleted,
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onGetDefaultState(): InquizeDetailUiState = InquizeDetailUiState()

    private fun onLoadInquizeDetailCompleted(inquizeBO: InquizeBO) {
        updateState {
            with(inquizeBO) {
                it.copy(
                    uid = uid,
                    imageUrl = imageUrl,
                    title = question,
                    description = imageDescription
                )
            }
        }
    }

    private fun onMapExceptionToState(ex: Exception, uiState: InquizeDetailUiState) =
        uiState.copy(
            isLoading = false
        )

    override fun onBackPressed() {
        launchSideEffect(InquizeDetailSideEffects.CloseDetail)
    }

    override fun onOpenChatClicked() {
        launchSideEffect(InquizeDetailSideEffects.OpenInquizeChat)
    }

    override fun onInquizeDeleted() {
        updateState { it.copy(showDeleteInquizeDialog = true) }
    }

    override fun onDeleteInquizeConfirmed() {
        updateState { it.copy(showDeleteInquizeDialog = false) }
        executeUseCaseWithParams(
            useCase = deleteInquizeByIdUseCase,
            params = DeleteInquizeByIdUseCase.Params(id = uiState.value.uid),
            onSuccess = { onDeleteInquizeCompleted() },
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onDeleteInquizeCancelled() {
        updateState { it.copy(showDeleteInquizeDialog = false) }
    }

    private fun onDeleteInquizeCompleted() {
        launchSideEffect(InquizeDetailSideEffects.InquizeDeleted)
    }
}

data class InquizeDetailUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val showDeleteInquizeDialog: Boolean = false,
    val infoMessage: String = String.EMPTY,
    val uid: String = String.EMPTY,
    val imageUrl: String = String.EMPTY,
    val title: String = String.EMPTY,
    val description: String = String.EMPTY
) : UiState<InquizeDetailUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): InquizeDetailUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface InquizeDetailSideEffects : SideEffect {
    data object CloseDetail: InquizeDetailSideEffects
    data object InquizeDeleted: InquizeDetailSideEffects
    data object OpenInquizeChat: InquizeDetailSideEffects
}