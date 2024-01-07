package com.speer.service.dto.response

data class LoginResponse(
    val message: String,
    val token: String?,
    val userResponse: UserResponse? = null
)

