package com.speer.service.service

import com.speer.service.dto.request.UserLogInRequest
import com.speer.service.dto.request.UserSignUpRequest
import com.speer.service.dto.response.LoginResponse
import com.speer.service.enum.UserCreationStatus
import com.speer.service.model.User

interface IUserService {
    fun login(request: UserLogInRequest): LoginResponse
    fun addUser(request: UserSignUpRequest): UserCreationStatus
    fun getUserByEmail(email: String): User

}