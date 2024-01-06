package com.speer.service.dto.request

import com.speer.service.model.User

data class UserSignUpRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
) {
    fun toUser(): User {
        return User(
            name = name,
            email = email,
            phone = phone,
            password = password
        )
    }
}
