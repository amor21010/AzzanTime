package com.omaraboesmail.entities


import androidx.annotation.Keep

@Keep
data class Designation(
    val abbreviated: String,
    val expanded: String
)