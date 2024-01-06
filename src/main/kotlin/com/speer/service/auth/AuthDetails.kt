package com.speer.service.auth

import com.speer.service.model.User
import com.speer.service.repository.IUserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class AuthDetails(
    private val iUserRepository: IUserRepository
) {
    fun getLoggedInUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = if (authentication != null && authentication.isAuthenticated) {
            (authentication.principal as UserDetails).username
        } else {
            null
        }
        if (userEmail.isNullOrBlank()) {
            throw Exception("Logged in user is invalid")
        }
        return iUserRepository.findByEmail(userEmail) ?: throw Exception("User with email $userEmail not found")
    }
}