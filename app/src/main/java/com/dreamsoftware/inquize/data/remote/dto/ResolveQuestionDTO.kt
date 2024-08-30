package com.dreamsoftware.inquize.data.remote.dto

data class ResolveQuestionDTO(
    val question: String,
    val imageUrl: String,
    val history: List<Pair<String, String>>
)
