package com.dreamsoftware.inquize.ui.screens.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieIconButton
import com.dreamsoftware.brownie.component.BrownieImageIcon
import com.dreamsoftware.brownie.component.BrownieImageSize
import com.dreamsoftware.brownie.component.BrownieSheetSurface
import com.dreamsoftware.brownie.component.BrownieText
import com.dreamsoftware.brownie.component.BrownieTextTypeEnum
import com.dreamsoftware.brownie.component.BrownieType
import com.dreamsoftware.brownie.component.screen.BrownieScreenContent
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.brownie.utils.clickWithRipple
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.domain.model.InquizeMessageBO
import com.dreamsoftware.inquize.domain.model.InquizeMessageRoleEnum
import com.dreamsoftware.inquize.ui.components.AnimatedMicButtonWithTranscript
import com.dreamsoftware.inquize.ui.components.ChatMessageCard
import com.dreamsoftware.inquize.ui.components.LoadingDialog
import com.dreamsoftware.inquize.ui.components.Role
import com.dreamsoftware.inquize.ui.theme.InquizeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreenContent(
    uiState: ChatUiState,
    actionListener: ChatScreenActionListener,
) {
    with(uiState) {
        with(MaterialTheme.colorScheme) {
            val chatScreenHaptics = rememberChatScreenHaptics()
            LoadingDialog(isShowingDialog = isLoading)
            BrownieScreenContent(
                hasTopBar = false,
                errorMessage = errorMessage,
                infoMessage = infoMessage,
                enableVerticalScroll = true,
                screenContainerColor = primary,
                onInfoMessageCleared = actionListener::onInfoMessageCleared,
                onErrorMessageCleared = actionListener::onErrorMessageCleared,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BrownieImageIcon(
                        modifier = Modifier.clickWithRipple {
                            actionListener.onBackButtonClicked()
                        },
                        type = BrownieType.ICON,
                        size = BrownieImageSize.LARGE,
                        iconRes = R.drawable.icon_arrow_left,
                        tintColor = Color.White
                    )
                    Image(
                        painter = painterResource(id = R.drawable.main_logo_inverse),
                        contentDescription = String.EMPTY,
                        modifier = Modifier
                            .height(70.dp)
                            .padding(bottom = 8.dp)
                    )
                    SoundToggleButton(
                        isAssistantMuted = isAssistantMuted,
                        onAssistantMutedChange = actionListener::onAssistantMutedChange
                    )
                }

                BrownieSheetSurface(
                    enableVerticalScroll = false,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Get the screen height from LocalConfiguration
                    val configuration = LocalConfiguration.current
                    val screenHeight = configuration.screenHeightDp.dp
                    Spacer(modifier = Modifier.height(8.dp))
                    ChatMessagesList(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(screenHeight * 0.62f) // Set 62% of the screen height
                            .padding(
                                start = 4.dp, end = 4.dp,
                                top = 8.dp, bottom = 0.dp
                            ),
                        messageList = messageList
                    )
                    Divider()
                    AnimatedContent(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        targetState = isAssistantResponseLoading,
                        label = String.EMPTY
                    ) { isResponseLoading ->
                        if (isResponseLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(64.dp)
                            )
                        } else if (isAssistantSpeaking) {
                            IconButton(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(80.dp),
                                onClick = {
                                    chatScreenHaptics.provideStopAssistantSpeechHapticFeedback()
                                    actionListener.onAssistantSpeechStopped()
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(80.dp),
                                    imageVector = ImageVector.vectorResource(R.drawable.baseline_stop_circle_24),
                                    contentDescription = null,
                                    tint = onPrimaryContainer
                                )
                            }
                        } else {
                            AnimatedMicButtonWithTranscript(
                                userTextTranscription = lastQuestion,
                                isListening = isListening,
                                onStartListening = actionListener::onStartListening
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessagesList(
    modifier: Modifier = Modifier,
    messageList: List<InquizeMessageBO>
) {
    val lazyListState = rememberLazyListState()
    if(messageList.isNotEmpty()) {
        LaunchedEffect(messageList) {
            lazyListState.animateScrollToItem(messageList.lastIndex)
        }
    }
    LazyColumn(
        modifier = modifier.fillMaxHeight(), // Ensures clear height constraints
        state = lazyListState
    ) {
        items(items = messageList, key = { it.uid }) { chatMessage ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ChatMessageCard(
                    modifier = Modifier
                        .align(
                            if (chatMessage.role == InquizeMessageRoleEnum.USER) {
                                Alignment.CenterEnd
                            } else {
                                Alignment.CenterStart
                            }
                        )
                        .widthIn(max = this.maxWidth / 1.5f),
                    messageContent = chatMessage.text,
                    role = if (chatMessage.role == InquizeMessageRoleEnum.USER) {
                        Role.USER
                    }  else {
                        Role.RESPONDER
                    }
                )
            }
        }
    }
}

@Composable
private fun SoundToggleButton(
    isAssistantMuted: Boolean,
    onAssistantMutedChange: (isMuted: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val chatScreenHaptics = rememberChatScreenHaptics()
    Row(
        modifier = modifier.animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BrownieIconButton(
            containerSize = 40.dp,
            iconRes = if(isAssistantMuted) {
                R.drawable.baseline_volume_off_24
            } else {
                R.drawable.baseline_volume_up_24
            }
        ) {
            onAssistantMutedChange(!isAssistantMuted)
            with(chatScreenHaptics) {
                if(isAssistantMuted) {
                    provideUnMutedHapticFeedback()
                } else {
                    provideMutedHapticFeedback()
                }
            }
        }
    }
}


@Composable
private fun rememberChatScreenHaptics(
    scope: CoroutineScope = rememberCoroutineScope(),
    localHapticFeedback: HapticFeedback = LocalHapticFeedback.current
): ChatScreenHaptics = remember(scope, localHapticFeedback) {
    ChatScreenHaptics(scope, localHapticFeedback)
}

private class ChatScreenHaptics(
    private val scope: CoroutineScope,
    private val localHapticFeedback: HapticFeedback
) {
    fun provideMutedHapticFeedback() {
        scope.launch {
            repeat(2) {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                delay(50)
            }
        }
    }

    fun provideUnMutedHapticFeedback() {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    fun provideStopAssistantSpeechHapticFeedback() {
        scope.launch {
            with(localHapticFeedback) {
                performHapticFeedback(HapticFeedbackType.LongPress)
                delay(5)
                performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }
}


@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun ChatScreenPreview() {
    InquizeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

        }
    }
}
