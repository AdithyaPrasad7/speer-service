package com.speer.service.dto.request

import com.speer.service.model.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

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
            password = BCryptPasswordEncoder().encode(password)
        )
    }
}
