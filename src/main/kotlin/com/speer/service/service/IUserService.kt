package com.speer.service.service

import com.speer.service.dto.request.UserLogInRequest
import com.speer.service.dto.request.UserSignUpRequest
import com.speer.service.enum.UserCreationStatus

interface IUserService {
    fun login(request: UserLogInRequest): String
    fun addUser(request: UserSignUpRequest): UserCreationStatus
}