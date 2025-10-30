package com.clockingcloud.data.remote.dto

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse
)