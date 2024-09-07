package com.dreamsoftware.inquize.ui.screens.settings

import com.dreamsoftware.brownie.component.BrownieSettingsItemVO
import com.dreamsoftware.brownie.core.IBrownieScreenActionListener

interface SettingsScreenActionListener: IBrownieScreenActionListener {
    fun onUpdateSheetVisibility(isVisible: Boolean)
    fun onUpdateCloseSessionDialogVisibility(isVisible: Boolean)
    fun onSettingItemClicked(item: BrownieSettingsItemVO)
    fun onCloseSession()
}