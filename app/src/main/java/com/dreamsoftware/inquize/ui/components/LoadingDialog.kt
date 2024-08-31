package com.dreamsoftware.inquize.ui.components

import androidx.compose.runtime.Composable
import com.dreamsoftware.brownie.component.BrownieLoadingDialog
import com.dreamsoftware.inquize.R

@Composable
fun LoadingDialog(isShowingDialog: Boolean) {
    BrownieLoadingDialog(
        isShowingDialog = isShowingDialog,
        mainLogoRes = R.drawable.main_logo,
        titleRes = R.string.loading_dialog_title_text,
        descriptionRes = R.string.loading_dialog_description_text
    )
}