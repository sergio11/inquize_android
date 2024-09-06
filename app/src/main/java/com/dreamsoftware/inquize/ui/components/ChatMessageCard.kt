package com.dreamsoftware.inquize.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieCard
import com.dreamsoftware.brownie.component.BrownieText
import com.dreamsoftware.brownie.component.BrownieTextTypeEnum

/**
 * Represents the role of a participant in a chat conversation.
 */
enum class Role {
    /**
     * Represents the user sending the message.
     */
    USER,

    /**
     * Represents the participant responding to the message.
     */
    RESPONDER
}

/**
 * A visually distinct card for a chat message based on the sender's [Role].
 *
 * @param messageContent the text content of the chat message.
 * @param role the [Role] of the message sender
 * @param modifier [Modifier] to be applied to the card composable.
 */
@Composable
fun ChatMessageCard(messageContent: String, role: Role, modifier: Modifier = Modifier) {
    with(MaterialTheme.colorScheme) {
        val border = remember(role) {
            BorderStroke(5.dp, if (role == Role.USER) {
                primaryContainer
            } else {
                primaryContainer
            })
        }
        val shape = remember(role) {
            RoundedCornerShape(50.dp).run {
                if (role == Role.USER) {
                    copy(topEnd = CornerSize(0.dp))
                } else {
                    copy(topStart = CornerSize(0.dp))
                }
            }
        }
        BrownieCard(
            modifier = modifier,
            colors = CardDefaults.outlinedCardColors(
                containerColor = if(role == Role.USER) {
                    primary
                } else {
                    secondary
                },
            ),
            border = border,
            shape = shape,
        ) {
            BrownieText(
                modifier = Modifier.padding(24.dp),
                type = BrownieTextTypeEnum.BODY_MEDIUM,
                titleText = messageContent,
                textColor = if(role == Role.USER) {
                    onPrimary
                } else {
                    onSecondary
                }
            )
        }
    }
}