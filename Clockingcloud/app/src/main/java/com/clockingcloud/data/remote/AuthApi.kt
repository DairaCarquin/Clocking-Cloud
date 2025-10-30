package com.clockingcloud.data.remote

import com.clockingcloud.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Field("refreshToken") refreshToken: String
    ): Response<Map<String, String>>
}
