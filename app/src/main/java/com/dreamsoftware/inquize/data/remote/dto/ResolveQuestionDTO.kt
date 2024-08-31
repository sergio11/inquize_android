package com.dreamsoftware.inquize.data.remote.dto

data class ResolveQuestionDTO(
    val question: String,
    val imageUrl: String,
    val history: List<QuestionHistoryMessageDTO>
)

data class QuestionHistoryMessageDTO(
    val role: String,
    val text: String
)
