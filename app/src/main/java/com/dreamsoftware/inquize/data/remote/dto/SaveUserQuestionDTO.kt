package com.dreamsoftware.inquize.data.remote.dto

data class SaveUserQuestionDTO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val prompt: String
)
