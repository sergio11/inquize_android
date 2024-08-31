package com.dreamsoftware.inquize.domain.model

import java.util.Date

data class InquizeBO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val createAt: Date,
    val question: String,
    val messages: List<InquizeMessageBO>
)
