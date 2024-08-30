package com.dreamsoftware.inquize.domain.model

data class ResolveQuestionBO(
    val question: String,
    val imageUrl: String,
    val history: List<Pair<String, String>>
)
