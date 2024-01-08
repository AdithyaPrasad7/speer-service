package com.speer.service.service

import com.speer.service.auth.AuthDetails
import com.speer.service.auth.JwtTokenUtil
import com.speer.service.dto.request.UserLogInRequest
import com.speer.service.dto.request.UserSignUpRequest
import com.speer.service.dto.response.LoginResponse
import com.speer.service.enum.UserCreationStatus
import com.speer.service.model.Notes
import com.speer.service.model.User
import com.speer.service.repository.IUserRepository
import com.speer.service.util.Validation
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class UserService(
    private val iUserRepository: IUserRepository,
    private var authManager: AuthenticationManager,
    val jwtUtil: JwtTokenUtil,
    private val authDetails: AuthDetails
) : IUserService {
    override fun login(request: UserLogInRequest): LoginResponse {
        val user = this.iUserRepository.findByEmail(request.email)
            ?: return LoginResponse(message = "User not found!", token = null)

        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val issuer = user.email
        val jwtToken = jwtUtil.generateToken(user.email)
        return LoginResponse(
            token = jwtToken,
            message = "User logged-in successfully",
            userResponse = user.toResponse()
        )
    }

    override fun addUser(request: UserSignUpRequest): UserCreationStatus {
        if (!Validation.isEmailValid(request.email)) {
            return UserCreationStatus.EMAIL_INVALID
        }
        if (!Validation.isPhoneValid(request.phone)) {
            return UserCreationStatus.PHONE_INVALID
        }

        var user: User? = iUserRepository.findByEmail(request.email)
        return if (user != null) {
            UserCreationStatus.USER_ALREADY_EXIST
        } else {

            val user = request.toUser()
            var savedUser = iUserRepository.save(user)
            UserCreationStatus.SUCCESS
        }
    }

    override fun getUserByEmail(email: String): User {
        var user = authDetails.getLoggedInUser()
        try {
            return iUserRepository.findByEmail(email)!!
        } catch (e: Exception) {
            return User(null, "Invalid User", "Invalid User", "Invalid User", "Invalid User", mutableListOf<Notes>())
        }
    }

}