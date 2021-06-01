package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class Hijri(
    val date: String,
    val day: String,
    val format: String,
    val month: Month,
    val year: String
)