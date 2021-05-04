package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class ApiResponce(
    val code: Int,
    val `data`: Data,
    val status: String
)