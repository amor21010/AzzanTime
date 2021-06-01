package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class AzzanApiResponse(
    val code: Int,
    val data: Data,
    val status: String
)