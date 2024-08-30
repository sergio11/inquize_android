package com.dreamsoftware.inquize.domain.model

data class SaveInquizeBO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val question: String,
    val answer: String
)
