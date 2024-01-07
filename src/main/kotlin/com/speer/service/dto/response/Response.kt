package com.speer.service.dto.response

class Response<T>(
    val message: String,
    val data: T? = null,
    error: Boolean? = false
) {

}