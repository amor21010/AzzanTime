package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class Method(
    val id: Int,
    val name: String,
    val params: Params
)