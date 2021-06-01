package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class Data(
    val date: Date,
    val timings: Timings
)