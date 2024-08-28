package com.dreamsoftware.inquize.ui.screens

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
import com.dreamsoftware.inquize.domain.model.ChatMessageBO
import com.dreamsoftware.inquize.ui.components.AnimatedMicButtonWithTranscript
import com.dreamsoftware.inquize.ui.components.ChatMessageCard
import com.dreamsoftware.inquize.ui.components.Role
import com.dreamsoftware.inquize.ui.theme.InquizeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    isAssistantResponseLoading: Boolean,
    chatMessageBOS: List<ChatMessageBO>,
    currentTranscription: String?,
    isListening: Boolean,
    onStartListening: () -> Unit,
    isAssistantMuted: Boolean,
    onAssistantMutedChange: (isMuted: Boolean) -> Unit,
    isAssistantSpeaking: Boolean,
    onStopAssistantSpeechButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chatScreenHaptics = rememberChatScreenHaptics()
    Column(
        modifier = modifier
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
                    onClick = onBackButtonClick,
                    content = {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                )
            },
            actions = {
                SoundToggleButton(
                    isAssistantMuted = isAssistantMuted,
                    onAssistantMutedChange = onAssistantMutedChange
                )
            }
        )
        ChatMessagesList(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            chatMessageBOS = chatMessageBOS
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
                        onStopAssistantSpeechButtonClick()
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
                    userTextTranscription = currentTranscription,
                    isListening = isListening,
                    onStartListening = onStartListening
                )
            }
        }
    }
}

@Composable
private fun ChatMessagesList(chatMessageBOS: List<ChatMessageBO>, modifier: Modifier = Modifier) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(chatMessageBOS) {
        lazyListState.animateScrollToItem(chatMessageBOS.lastIndex)
    }
    LazyColumn(modifier = modifier, state = lazyListState) {
        items(items = chatMessageBOS, key = { it.id }) { chatMessage ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val alignment = remember {
                    if (chatMessage.role == ChatMessageBO.Role.USER) Alignment.CenterEnd
                    else Alignment.CenterStart
                }
                ChatMessageCard(
                    modifier = Modifier
                        .align(alignment = alignment)
                        .widthIn(max = this.maxWidth / 1.5f),
                    messageContent = chatMessage.message,
                    role = if (chatMessage.role == ChatMessageBO.Role.USER) Role.USER
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
            ChatScreen(
                isAssistantResponseLoading = false,
                chatMessageBOS = listOf(
                    ChatMessageBO(
                        id = "1",
                        message = "Hello, how can I help you?",
                        role = ChatMessageBO.Role.ASSISTANT
                    ),
                    ChatMessageBO(
                        id = "2",
                        message = "I'm looking for a good place to eat",
                        role = ChatMessageBO.Role.USER
                    )
                ),
                currentTranscription = "I'm looking for a good place to eat",
                isListening = false,
                onStartListening = {},
                isAssistantMuted = false,
                onAssistantMutedChange = {},
                isAssistantSpeaking = true,
                onStopAssistantSpeechButtonClick = {},
                onBackButtonClick = {}
            )
        }
    }
}
