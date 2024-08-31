package com.dreamsoftware.inquize.ui.screens.settings

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener

interface SettingsScreenActionListener: IBrownieScreenActionListener {
    fun onUpdateSheetVisibility(isVisible: Boolean)
    fun onUpdateCloseSessionDialogVisibility(isVisible: Boolean)
    fun onSettingItemClicked(item: SettingsItem)
    fun onCloseSession()
}