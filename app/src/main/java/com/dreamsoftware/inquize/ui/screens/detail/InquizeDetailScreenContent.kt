package com.dreamsoftware.inquize.ui.screens.detail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieButton
import com.dreamsoftware.brownie.component.BrownieButtonStyleTypeEnum
import com.dreamsoftware.brownie.component.BrownieButtonTypeEnum
import com.dreamsoftware.brownie.component.BrownieDialog
import com.dreamsoftware.brownie.component.BrownieText
import com.dreamsoftware.brownie.component.BrownieTextTypeEnum
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.components.CommonDetailScreen

@Composable
internal fun InquizeDetailScreenContent(
    context: Context,
    density: Density,
    scrollState: ScrollState,
    uiState: InquizeDetailUiState,
    actionListener: InquizeDetailScreenActionListener
) {
    with(uiState) {
        BrownieDialog(
            isVisible = showDeleteInquizeDialog,
            mainLogoRes = R.drawable.main_logo,
            titleRes = R.string.delete_inquize_dialog_title,
            descriptionRes = R.string.delete_inquize_dialog_description,
            cancelRes = R.string.delete_inquize_dialog_cancel,
            acceptRes = R.string.delete_inquize_dialog_accept,
            onCancelClicked = actionListener::onDeleteInquizeCancelled,
            onAcceptClicked = actionListener::onDeleteInquizeConfirmed,
        )
        CommonDetailScreen(
            context = context,
            errorMessage = errorMessage,
            infoMessage = infoMessage,
            isLoading = isLoading,
            density = density,
            imageUrl = imageUrl,
            title = title,
            scrollState = scrollState,
            defaultImagePlaceholderRes = R.drawable.ic_placeholder,
            onBackClicked = actionListener::onBackPressed
        ) {
            BrownieText(
                type = BrownieTextTypeEnum.BODY_MEDIUM,
                titleText = description
            )
            Spacer(modifier = Modifier.height(30.dp))
            BrownieButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                type = BrownieButtonTypeEnum.LARGE,
                onClick = actionListener::onOpenChatClicked,
                textRes = R.string.inquize_detail_open_chat_text
            )
            BrownieButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                type = BrownieButtonTypeEnum.LARGE,
                style = BrownieButtonStyleTypeEnum.DANGER,
                onClick = actionListener::onInquizeDeleted,
                textRes = R.string.inquize_detail_delete_button_text
            )
        }
    }
}