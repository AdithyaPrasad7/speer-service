package com.speer.service.controller

import com.speer.service.dto.request.UserLogInRequest
import com.speer.service.dto.request.UserSignUpRequest
import com.speer.service.dto.response.Response
import com.speer.service.enum.UserCreationStatus
import com.speer.service.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class UserController(private val `userService`: UserService) {

    @PostMapping("/login")
    fun login(@RequestBody request: UserLogInRequest): ResponseEntity<Response<String>> {
        var response = userService.login(request)
        return ResponseEntity.ok(
            Response(
                message = "Login Successful"
            )
        )
    }

    @PostMapping("/signup")
    fun addUser(@RequestBody request: UserSignUpRequest): ResponseEntity<Response<String>> {
        var response = userService.addUser(request)
        val message = response.message
        if (response == UserCreationStatus.SUCCESS)
            return ResponseEntity(Response(message = message), HttpStatus.CREATED)
        else
            return ResponseEntity(Response(message = message), HttpStatus.BAD_REQUEST)


    }

}