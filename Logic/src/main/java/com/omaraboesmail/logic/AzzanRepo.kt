package com.omaraboesmail.logic

import com.omaraboesmail.entities.AzzanTimesQuery
import javax.inject.Inject

class AzzanRepo @Inject constructor(private val azzanInterface: AzzanInterface) {
    suspend fun getAzzanTimes(
      query: AzzanTimesQuery
    ) =
        if (query.city != null) {
            azzanInterface.getAzzanTimesByCity(query.city!!)
        } else
            azzanInterface.getAzzanTimesByLocation(
                latitude = query.location!!.latitude.toString(),
                longitude = query.location!!.longitude.toString()
            )

}