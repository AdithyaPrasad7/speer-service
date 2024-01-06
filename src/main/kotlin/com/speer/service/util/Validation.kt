package com.speer.service.util

object Validation {
    fun isEmailValid(email: String): Boolean {
        val validEmail = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
        return email.matches(validEmail)
    }

    fun isPhoneValid(phone: String): Boolean {
        val validNumber = "^[0-9]{10}$".toRegex()
        return phone.matches(validNumber)

    }
}