package com.dreamsoftware.inquize.domain.model

data class AddInquizeMessageBO(
    val uid: String,
    val userId: String,
    val question: String,
    val answer: String
)
