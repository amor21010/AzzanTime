package com.omaraboesmail.logic

import javax.inject.Inject

class AzzanRepo @Inject constructor(private val azzanInterface: AzzanInterface) {
    suspend fun getAzzanTimes(city: String = "cairo") = azzanInterface.getAzzanTimesByCity(city)
}