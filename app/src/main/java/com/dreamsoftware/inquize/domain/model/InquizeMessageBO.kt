package com.dreamsoftware.inquize.domain.model

data class InquizeMessageBO(
    val uid: String,
    val role: InquizeMessageRoleEnum,
    val text: String
)
