package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class Date(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)