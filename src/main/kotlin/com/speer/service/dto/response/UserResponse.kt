package com.speer.service.dto.response

import com.speer.service.model.Notes

data class UserResponse(
    val name: String,
    val email: String,
    val phone: String,
    val notes: List<Notes>?
)
