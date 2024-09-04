package com.dreamsoftware.inquize.data.remote.dto

data class CreateInquizeDTO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val imageDescription: String,
    val question: String,
    val questionRole: String,
    val answer: String,
    val answerRole: String
)
