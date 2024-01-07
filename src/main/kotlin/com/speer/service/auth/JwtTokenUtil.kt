package com.speer.service.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import java.security.Key

@Component
class JwtTokenUtil {
    private val secret = "NPAw9z$&F)J@NcRfUjXn2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$&E(H+MbQeTAhWmZq\n"
    private val expiration = 60000000 // milliseconds

    fun generateToken(email: String): String {
        val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())
        // Create JWT token
        return Jwts.builder()
            .setSubject(email)
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(secretKey).compact()
    }

    fun getEmail(jwt: String): String {
        try {
            var jwtBody = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build().parseClaimsJws(jwt).body
            var email = jwtBody.subject
            return email
        } catch (e: Exception) {
            // Handle the error and send a 401 status code
            return "401 Unauthorized"
        }
    }

    fun isTokenValid(jwt: String?): Boolean {
        try {
            if (jwt == null) {
                return false
            }
            val body = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwt).body
            return true;
        } catch (e: Exception) {
            return false
        }
    }

    private fun getSignInKey(): Key? {
        return Keys.hmacShaKeyFor(secret.toByteArray())
    }

}