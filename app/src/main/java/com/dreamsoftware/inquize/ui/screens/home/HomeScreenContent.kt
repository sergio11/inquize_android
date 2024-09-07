package com.dreamsoftware.inquize.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieCard
import com.dreamsoftware.brownie.component.BrownieColumnPlaceHolder
import com.dreamsoftware.brownie.component.BrownieColumnProgressIndicator
import com.dreamsoftware.brownie.component.BrownieDefaultTextField
import com.dreamsoftware.brownie.component.BrownieDialog
import com.dreamsoftware.brownie.component.BrownieIconButton
import com.dreamsoftware.brownie.component.BrownieSheetSurface
import com.dreamsoftware.brownie.component.BrownieText
import com.dreamsoftware.brownie.component.BrownieTextTypeEnum
import com.dreamsoftware.brownie.component.screen.BrownieScreenContent
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.screens.core.CommonInquizeImage

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    actionListener: HomeScreenActionListener
) {
    with(uiState) {
        with(MaterialTheme.colorScheme) {
            val isVisible = rememberSaveable { mutableStateOf(true) }
            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        // Hide FAB
                        if (available.y < -1) {
                            isVisible.value = false
                        }

                        // Show FAB
                        if (available.y > 1) {
                            isVisible.value = true
                        }

                        return Offset.Zero
                    }
                }
            }
            BrownieDialog(
                isVisible = confirmDeleteInquize != null,
                mainLogoRes = R.drawable.main_logo,
                titleRes = R.string.delete_inquize_dialog_title,
                descriptionRes = R.string.delete_inquize_dialog_description,
                cancelRes = R.string.delete_inquize_dialog_cancel,
                acceptRes = R.string.delete_inquize_dialog_accept,
                onCancelClicked = actionListener::onDeleteInquizeCancelled,
                onAcceptClicked = actionListener::onDeleteInquizeConfirmed,
            )
            BrownieScreenContent(
                hasTopBar = false,
                infoMessage = infoMessage,
                screenContainerColor = primary,
                onErrorMessageCleared = actionListener::onErrorMessageCleared,
                onInfoMessageCleared = actionListener::onInfoMessageCleared
            ) {
                Image(
                    painter = painterResource(id = R.drawable.main_logo_inverse),
                    contentDescription = String.EMPTY,
                    modifier = Modifier
                        .height(90.dp)
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                BrownieSheetSurface(
                    enableVerticalScroll = false,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BrownieDefaultTextField(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp, end = 6.dp,
                                    top = 6.dp, bottom = 8.dp
                                )
                                .fillMaxWidth(),
                            labelRes = R.string.home_search_text_input_label,
                            placeHolderRes = R.string.home_search_text_input_placeholder,
                            value = searchQuery,
                            onValueChanged = {
                                if (it.length <= 25) {
                                    actionListener.onSearchQueryUpdated(newSearchQuery = it)
                                }
                            },
                            leadingIconRes = R.drawable.icon_search,
                            isSingleLine = true,
                        )
                    }
                    if (isLoading) {
                        BrownieColumnProgressIndicator(textIndicatorRes = R.string.content_loading_placeholder)
                    } else if (inquizeList.isEmpty()) {
                        BrownieColumnPlaceHolder(
                            titleRes = R.string.nothing_found,
                            iconRes = R.drawable.ic_no_data_found
                        )
                    } else {
                        InquizeList(
                            uiState = uiState,
                            actionListener = actionListener,
                            nestedScrollConnection = nestedScrollConnection
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun InquizeList(
    uiState: HomeUiState,
    actionListener: HomeScreenActionListener,
    nestedScrollConnection: NestedScrollConnection
) {
    with(MaterialTheme.colorScheme) {
        with(uiState) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 4.dp, end = 4.dp,
                        top = 8.dp, bottom = 0.dp
                    )
                    .nestedScroll(nestedScrollConnection),
            ) {
                items(inquizeList.size) { idx ->
                    val inquize = inquizeList[idx]
                    BrownieCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .height(250.dp)
                            .clickable {
                                actionListener.onInquizeClicked(inquize)
                            },
                        border = BorderStroke(5.dp, secondary)
                    ) {
                        CommonInquizeImage(
                            modifier = Modifier.fillMaxSize(),
                            imageUrl = inquize.imageUrl
                        )
                        Row(
                            modifier = Modifier
                                .background(secondary.copy(0.3f))
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BrownieIconButton(
                                containerSize = 40.dp,
                                containerColor = onPrimary,
                                iconTintColor = primary,
                                iconRes = R.drawable.ic_detail
                            ) {
                                actionListener.onInquizeDetailClicked(inquize)
                            }
                            BrownieIconButton(
                                containerSize = 40.dp,
                                containerColor = onPrimary,
                                iconTintColor = primary,
                                iconRes = R.drawable.ic_delete_inquize
                            ) {
                                actionListener.onInquizeDeleted(inquize)
                            }
                        }
                        BrownieText(
                            modifier = Modifier
                                .background(secondary)
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 20.dp)
                                .align(Alignment.BottomStart),
                            type = BrownieTextTypeEnum.LABEL_MEDIUM,
                            titleText = inquize.question,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            textBold = true,
                            textColor = onSecondary
                        )
                    }
                }
            }
        }
    }
}