package com.omaraboesmail.logic

import com.omaraboesmail.entities.AzzanApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AzzanInterface {
    @GET("/v1/timingsByCity")
    suspend fun getAzzanTimesByCity(
        @Query("city") city: String,
        @Query("country") country: String = "Egypt",
        @Query("method") method:String="8"
    ): AzzanApiResponse
}