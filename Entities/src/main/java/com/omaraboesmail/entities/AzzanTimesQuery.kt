package com.omaraboesmail.entities

import android.location.Location

data class AzzanTimesQuery(
    var city: String? = null,
    var timestamp: String? = null,
    var location: Location? = null
)
