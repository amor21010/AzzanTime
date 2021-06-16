package com.omaraboesmail.logic

import com.omaraboesmail.entities.AzzanApiResponse
import com.omaraboesmail.logic.utils.getCurrentTime
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

interface AzzanInterface {
    @GET("/v1/timingsByCity")
    suspend fun getAzzanTimesByCity(
        @Query("city") city: String,
        @Query("country") country: String = "Egypt",
        @Query("method") method: String = "8"
    ): AzzanApiResponse

    @GET(value = "/v1/timings/{timeStamp}")
    suspend fun getAzzanTimesByLocation(
        @Path("timeStamp") timeStamp: String =Timestamp(System.currentTimeMillis()).toString() ,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
    ):AzzanApiResponse
}