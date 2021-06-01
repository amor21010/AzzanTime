package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class Timings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Sunset: String,
    val Maghrib: String,
    val Isha: String,
    val Imsak: String,
    val Midnight: String
) {

    fun getTimingAsList(): List<String> {
        return listOf(
            "Fajr : /$Fajr",
            "Sunrise : /$Sunrise",
            "Dhuhr : /$Dhuhr",
            "Asr : /$Asr",
            "Maghrib : /$Maghrib",
            "Isha : /$Isha"
        )
    }
}