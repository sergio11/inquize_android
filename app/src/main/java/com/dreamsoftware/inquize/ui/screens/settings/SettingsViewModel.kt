package com.dreamsoftware.inquize.ui.screens.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.BrownieEventBus
import com.dreamsoftware.inquize.AppEvent
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.domain.model.AuthUserBO
import com.dreamsoftware.inquize.domain.usecase.GetAuthenticateUserDetailUseCase
import com.dreamsoftware.inquize.domain.usecase.SignOffUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOffUseCase: SignOffUseCase,
    private val getAuthenticateUserDetailUseCase: GetAuthenticateUserDetailUseCase,
    private val brownieEventBus: BrownieEventBus
) : BrownieViewModel<SettingsUiState, SettingsUiSideEffects>(), SettingsScreenActionListener {

    override fun onGetDefaultState(): SettingsUiState = SettingsUiState(
        items = buildItems()
    )

    fun onInit() {
        executeUseCase(
            useCase = getAuthenticateUserDetailUseCase,
            onSuccess = ::onAuthenticatedUserLoadSuccessfully
        )
    }

    override fun onUpdateSheetVisibility(isVisible: Boolean) {
        updateState { it.copy(showSheet = isVisible) }
    }

    override fun onUpdateCloseSessionDialogVisibility(isVisible: Boolean) {
        updateState { it.copy(showCloseSessionDialog = isVisible) }
    }

    override fun onSettingItemClicked(item: SettingsItem) {
        when(item) {
            AboutItem -> onUpdateSheetVisibility(isVisible = true)
            LogoutItem -> onUpdateCloseSessionDialogVisibility(isVisible = true)
            ShareItem -> launchSideEffect(SettingsUiSideEffects.ShareApp)
        }
    }

    override fun onCloseSession() {
        onUpdateCloseSessionDialogVisibility(isVisible = false)
        executeUseCase(useCase = signOffUseCase)
        brownieEventBus.send(AppEvent.SignOff)
    }

    private fun buildItems() = buildList {
        add(ShareItem)
        add(AboutItem)
        add(LogoutItem)
    }

    private fun onAuthenticatedUserLoadSuccessfully(authUserBO: AuthUserBO) {
        updateState { it.copy(authUserBO = authUserBO) }
    }
}

data class SettingsUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val showSheet: Boolean = false,
    val showCloseSessionDialog: Boolean = false,
    val items: List<SettingsItem> = emptyList(),
    val authUserBO: AuthUserBO? = null
): UiState<SettingsUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): SettingsUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

abstract class SettingsItem(
    @StringRes open val textRes: Int,
    @DrawableRes open val icon: Int,
    open val isDanger: Boolean = false
)

abstract class SettingsItemSwitch(
    val isEnabled: Boolean,
    override val textRes: Int,
    override val icon: Int,
    override val isDanger: Boolean
): SettingsItem(textRes, icon, isDanger)

data object ShareItem: SettingsItem(textRes = R.string.settings_screen_share, icon = R.drawable.icon_share)
data object AboutItem: SettingsItem(textRes = R.string.settings_screen_about, icon = R.drawable.icon_info)
data object LogoutItem: SettingsItem(textRes = R.string.settings_screen_logout, icon = R.drawable.icon_logout, isDanger = true)

sealed interface SettingsUiSideEffects: SideEffect {
    data object ShareApp: SettingsUiSideEffects
}