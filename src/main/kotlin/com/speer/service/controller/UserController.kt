package com.speer.service.controller

import com.speer.service.dto.request.UserLogInRequest
import com.speer.service.dto.request.UserSignUpRequest
import com.speer.service.dto.response.LoginResponse
import com.speer.service.dto.response.Response
import com.speer.service.enum.UserCreationStatus
import com.speer.service.service.UserService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class UserController(private val `userService`: UserService) {

    @PostMapping("/login")
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun login(@RequestBody request: UserLogInRequest): ResponseEntity<LoginResponse> {
        var response = userService.login(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/signup")
    @RateLimiter(name = "Controller", fallbackMethod = "rateLimitFallback")
    fun addUser(@RequestBody request: UserSignUpRequest): ResponseEntity<Response<String>> {
        var response = userService.addUser(request)
        val message = response.message
        if (response == UserCreationStatus.SUCCESS)
            return ResponseEntity(Response(message = message), HttpStatus.CREATED)
        else
            return ResponseEntity(Response(message = message), HttpStatus.BAD_REQUEST)
    }

    fun rateLimitFallback(e: Exception?): ResponseEntity<String> {
        return ResponseEntity("User Controller Rate Exceeded", HttpStatus.TOO_MANY_REQUESTS)
    }
}