package com.dreamsoftware.inquize.domain.model

data class AuthUserBO(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String
)