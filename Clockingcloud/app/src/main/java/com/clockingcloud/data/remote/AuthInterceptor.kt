package com.clockingcloud.data.remote

import android.util.Log
import com.clockingcloud.utils.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val session: SessionManager,
    private val api: AuthApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        session.getAccessToken()?.let {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $it")
                .build()
        }

        var response = chain.proceed(request)

        if (response.code == 401) {
            Log.w("AuthInterceptor", "Access token expirado. Intentando refresh...")

            val refreshToken = session.getRefreshToken()
            if (refreshToken != null) {
                val newToken = try {
                    kotlinx.coroutines.runBlocking(kotlinx.coroutines.Dispatchers.IO) {
                        val refreshResponse = api.refreshToken(refreshToken)
                        if (refreshResponse.isSuccessful) {
                            val body = refreshResponse.body()
                            body?.get("accessToken")
                        } else null
                    }
                } catch (e: Exception) {
                    null
                }

                if (newToken != null) {
                    response.close()
                    val newRequest = request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $newToken")
                        .build()
                    response = chain.proceed(newRequest)
                } else {
                    Log.e("AuthInterceptor", "No se pudo renovar el token. Cerrando sesión.")
                    session.clearSession()
                }
            } else {
                Log.e("AuthInterceptor", "No hay refresh token disponible.")
                session.clearSession()
            }
        }

        return response
    }
}