package com.dreamsoftware.inquize.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    val shape = remember(role) {
        if (role == Role.USER) RoundedCornerShape(50.dp).copy(topEnd = CornerSize(0.dp))
        else RoundedCornerShape(50.dp).copy(topStart = CornerSize(0.dp))
    }
    OutlinedCard(
        modifier = modifier,
        shape = shape,
        content = { Text(modifier = Modifier.padding(24.dp), text = messageContent) }
    )
}