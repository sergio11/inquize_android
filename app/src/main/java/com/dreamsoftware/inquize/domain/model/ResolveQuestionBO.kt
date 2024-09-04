package com.dreamsoftware.inquize.domain.model

data class ResolveQuestionBO(
    val question: String,
    val context: String,
    val history: List<Pair<String, String>> = emptyList()
)
