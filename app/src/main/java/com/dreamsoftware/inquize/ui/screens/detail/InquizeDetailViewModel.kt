package com.dreamsoftware.inquize.ui.screens.detail

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.domain.usecase.DeleteInquizeByIdUseCase
import com.dreamsoftware.inquize.domain.usecase.GetInquizeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InquizeDetailViewModel @Inject constructor(
    private val getInquizeByIdUseCase: GetInquizeByIdUseCase,
    private val deleteInquizeByIdUseCase: DeleteInquizeByIdUseCase,
) : BrownieViewModel<InquizeDetailUiState, InquizeDetailSideEffects>(),
    InquizeDetailScreenActionListener {

    override fun onGetDefaultState(): InquizeDetailUiState = InquizeDetailUiState()

    private fun onMapExceptionToState(ex: Exception, uiState: InquizeDetailUiState) =
        uiState.copy(
            isLoading = false,
            imageUrl = String.EMPTY,
            question = String.EMPTY
        )
}

data class InquizeDetailUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val infoMessage: String = String.EMPTY,
    val imageUrl: String = String.EMPTY,
    val question: String = String.EMPTY
) : UiState<InquizeDetailUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): InquizeDetailUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface InquizeDetailSideEffects : SideEffect