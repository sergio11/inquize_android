package com.dreamsoftware.inquize.domain.model

data class CreateInquizeBO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val imageDescription: String,
    val question: String,
    val answer: String
)
