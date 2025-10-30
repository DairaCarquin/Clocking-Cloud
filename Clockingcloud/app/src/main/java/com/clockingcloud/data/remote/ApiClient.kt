package com.clockingcloud.data.remote

import android.content.Context
import com.clockingcloud.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8845/api/"

    fun getInstance(context: Context): Retrofit {
        val session = SessionManager(context)

        val baseRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authApi = baseRetrofit.create(AuthApi::class.java)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(session, authApi))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
