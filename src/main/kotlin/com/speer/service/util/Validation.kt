package com.speer.service.util

import io.jsonwebtoken.Jwts

object Validation {
    fun isEmailValid(email: String): Boolean {
        val validEmail = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
        return email.matches(validEmail)
    }

    fun isPhoneValid(phone: String): Boolean {
        val validNumber = "^[0-9]{10}$".toRegex()
        return phone.matches(validNumber)

    }

    fun validateJwtToken(jwt: String?): String {
        try {
            if (jwt == null) {
                return "Unauthenticated"
            }

            val body = Jwts.parser().setSigningKey("secret").parseClaimsJwt(jwt).body
            return body["id"].toString();
        } catch (e: Exception) {
            return "Unauthenticated"
        }
    }
}