package com.clockingcloud.data.remote

import com.clockingcloud.data.remote.dto.AttendanceResponse
import com.clockingcloud.data.remote.dto.MessageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class AttendanceDay(
    val date: String,
    val status: String
)
interface AttendanceApi {

    @GET("attendance/last")
    suspend fun getLastEvent(@Query("userId") userId: Int): Response<AttendanceResponse>

    @POST("attendance/checkin")
    suspend fun checkIn(@Query("userId") userId: Int): Response<MessageResponse>

    @POST("attendance/checkout")
    suspend fun checkOut(@Query("userId") userId: Int): Response<MessageResponse>

    @GET("attendance/monthly")
    suspend fun getMonthlyAttendance(
        @Query("userId") userId: Long,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<List<AttendanceDay>>
}
