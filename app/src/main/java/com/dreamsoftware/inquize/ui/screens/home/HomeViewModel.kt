package com.dreamsoftware.inquize.ui.screens.home

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.di.HomeErrorMapper
import com.dreamsoftware.inquize.domain.model.InquizeBO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @HomeErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<HomeUiState, HomeSideEffects>(), HomeScreenActionListener {

    fun loadData() {

    }

    override fun onGetDefaultState(): HomeUiState = HomeUiState()

    private fun onMapExceptionToState(ex: Exception, uiState: HomeUiState) =
        uiState.copy(
            isLoading = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )

    override fun onInfoMessageCleared() {
        updateState { it.copy(infoMessage = null) }
    }
}

data class HomeUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val infoMessage: String? = null,
    val inquizeList: List<InquizeBO> = emptyList(),
    val searchQuery: String = String.EMPTY
): UiState<HomeUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): HomeUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}


sealed interface HomeSideEffects: SideEffect