package com.speer.service.service

import com.speer.service.dto.request.UserLogInRequest
import com.speer.service.dto.request.UserSignUpRequest
import com.speer.service.enum.UserCreationStatus
import com.speer.service.model.User
import com.speer.service.repository.IUserRepository
import com.speer.service.util.Validation
import org.springframework.stereotype.Service

@Service
class UserService(
    private val iUserRepository: IUserRepository
) : IUserService {
    override fun login(request: UserLogInRequest): String {
        val user = iUserRepository.findByEmail(request.email)
        if (user != null && user.password != request.password) {
            return "Incorrect Password"
        } else {
            return "User Not Found"
        }

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
}