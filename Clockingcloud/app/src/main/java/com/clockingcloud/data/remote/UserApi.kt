package com.clockingcloud.data.remote

import com.clockingcloud.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
interface UserApi {
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<UserResponse>
}