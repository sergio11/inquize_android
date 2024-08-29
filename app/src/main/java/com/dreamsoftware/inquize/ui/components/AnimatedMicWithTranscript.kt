package com.dreamsoftware.inquize.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieText
import com.dreamsoftware.brownie.component.BrownieTextTypeEnum

/**
 * This composable is an extension of the [AnimatedMicButton] that also displays the
 * [userTextTranscription] above the button.
 */
@Composable
fun AnimatedMicButtonWithTranscript(
    userTextTranscription: String?,
    isListening: Boolean,
    onStartListening: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AnimatedVisibility(visible = !userTextTranscription.isNullOrBlank(), enter = fadeIn()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                BrownieText(
                    modifier = Modifier
                        .padding(16.dp)
                        .animateContentSize(),
                    type = BrownieTextTypeEnum.TITLE_LARGE,
                    titleText = userTextTranscription
                )
            }
        }
        AnimatedMicButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            isAnimationRunning = isListening,
            onClick = onStartListening
        )
    }
}