package com.clockingcloud.data.remote.dto

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String?,
    val role: String,
    val profilePhoto: String?
)