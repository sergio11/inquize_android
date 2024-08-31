package com.dreamsoftware.inquize.data.remote.dto

data class CreateInquizeDTO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val question: String,
    val answer: String
)
