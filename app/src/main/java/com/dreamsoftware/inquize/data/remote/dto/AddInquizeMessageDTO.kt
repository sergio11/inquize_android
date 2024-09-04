package com.dreamsoftware.inquize.data.remote.dto

data class AddInquizeMessageDTO(
    val uid: String,
    val userId: String,
    val question: String,
    val questionRole: String,
    val answer: String,
    val answerRole: String
)
