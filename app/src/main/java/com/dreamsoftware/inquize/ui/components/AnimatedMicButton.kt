package com.dreamsoftware.inquize.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.theme.InquizeTheme
import com.dreamsoftware.inquize.ui.theme.RoundedStarShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Creates an animated microphone button that has a rotating "curvy circle" background.
 *
 * @param isAnimationRunning boolean that determines whether the microphone animation is active.
 * @param onClick callback to invoked when the button is clicked.
 * @param modifier [Modifier] to be applied to the composable.
 */
@Composable
fun AnimatedMicButton(
    isAnimationRunning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    with(MaterialTheme.colorScheme) {
        val infiniteTransition = rememberInfiniteTransition(label = String.EMPTY)
        val animatedCurrentRotationDegrees by infiniteTransition.animateFloat(
            label = String.EMPTY,
            initialValue = 0f,
            targetValue = if (isAnimationRunning) 360f else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5_000, easing = LinearEasing)
            ),
        )
        val localHapticFeedback = LocalHapticFeedback.current
        val scope = rememberCoroutineScope()
        Button(
            modifier = modifier,
            onClick = {
                scope.launch {
                    // custom haptic feedback
                    repeat(2) {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        delay(100)
                    }
                }
                onClick()
            },
            shape = RoundedStarShape(rotation = animatedCurrentRotationDegrees),
            colors = ButtonDefaults.buttonColors(containerColor = if(isAnimationRunning) {
                secondary
            } else {
                primary
            })
        ) {
            Icon(
                modifier = Modifier
                    .padding(32.dp)
                    .size(32.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_mic_24),
                tint = if(isAnimationRunning) {
                    onSecondary
                } else {
                    onPrimary
                },
                contentDescription = null,
            )
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun AnimatedMicButtonPreview() {
    InquizeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = { AnimatedMicButton(isAnimationRunning = false, onClick = {}) }
        )
    }
}