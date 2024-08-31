package com.dreamsoftware.inquize.data.remote.dto

import com.google.firebase.Timestamp

data class InquizeDTO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val createAt: Timestamp,
    val messages: List<InquizeMessageDTO>
)