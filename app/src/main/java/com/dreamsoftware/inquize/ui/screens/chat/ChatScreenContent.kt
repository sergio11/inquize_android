package com.dreamsoftware.inquize.ui.screens.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.domain.model.InquizeMessageBO
import com.dreamsoftware.inquize.domain.model.InquizeMessageRoleEnum
import com.dreamsoftware.inquize.ui.components.AnimatedMicButtonWithTranscript
import com.dreamsoftware.inquize.ui.components.ChatMessageCard
import com.dreamsoftware.inquize.ui.components.Role
import com.dreamsoftware.inquize.ui.theme.InquizeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenContent(
    uiState: ChatUiState,
    actionListener: ChatScreenActionListener,
) {
    with(uiState) {
        val chatScreenHaptics = rememberChatScreenHaptics()
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Gemini",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = actionListener::onBackButtonClicked,
                        content = {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    )
                },
                actions = {
                    SoundToggleButton(
                        isAssistantMuted = isAssistantMuted,
                        onAssistantMutedChange = actionListener::onAssistantMutedChange
                    )
                }
            )
            ChatMessagesList(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                messageList = messageList
            )
            Divider()
            AnimatedContent(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                targetState = isAssistantResponseLoading,
                label = ""
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
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
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

@Composable
private fun ChatMessagesList(
    modifier: Modifier = Modifier,
    messageList: List<InquizeMessageBO>
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(messageList) {
        lazyListState.animateScrollToItem(messageList.lastIndex)
    }
    LazyColumn(modifier = modifier, state = lazyListState) {
        items(items = messageList, key = { it.uid }) { chatMessage ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val alignment = remember {
                    if (chatMessage.role == InquizeMessageRoleEnum.USER) Alignment.CenterEnd
                    else Alignment.CenterStart
                }
                ChatMessageCard(
                    modifier = Modifier
                        .align(alignment = alignment)
                        .widthIn(max = this.maxWidth / 1.5f),
                    messageContent = chatMessage.text,
                    role = if (chatMessage.role == InquizeMessageRoleEnum.USER) Role.USER
                    else Role.RESPONDER
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
    val volumeOffIcon = ImageVector.vectorResource(id = R.drawable.baseline_volume_off_24)
    val volumeUpIcon = ImageVector.vectorResource(id = R.drawable.baseline_volume_up_24)

    Row(
        modifier = modifier.animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isAssistantMuted) {
            IconButton(
                onClick = {
                    onAssistantMutedChange(false)
                    chatScreenHaptics.provideUnMutedHapticFeedback()
                },
                content = { Icon(imageVector = volumeOffIcon, contentDescription = null) }
            )
        } else {
            IconButton(
                onClick = {
                    onAssistantMutedChange(true)
                    chatScreenHaptics.provideMutedHapticFeedback()
                },
                content = { Icon(imageVector = volumeUpIcon, contentDescription = null) }
            )
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
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(5)
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
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
