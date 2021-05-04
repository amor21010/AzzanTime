package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class Timings(
    val Asr: String,
    val Dhuhr: String,
    val Fajr: String,
    val Imsak: String,
    val Isha: String,
    val Maghrib: String,
    val Midnight: String,
    val Sunrise: String,
    val Sunset: String
)