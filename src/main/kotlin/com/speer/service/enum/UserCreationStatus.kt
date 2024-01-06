package com.speer.service.enum

enum class UserCreationStatus(val message: String) {
    SUCCESS("User successfully created"),
    EMAIL_INVALID("Invalid email"),
    PHONE_INVALID("Invalid contact number details"),
    PASSWORD_INVALID("Password is invalid"),
    USER_ALREADY_EXIST("User already exist")
}