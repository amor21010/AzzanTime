package com.omaraboesmail.azan.ui

import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omaraboesmail.entities.AzzanTimesQuery
import com.omaraboesmail.entities.Data
import com.omaraboesmail.entities.Timings
import com.omaraboesmail.logic.AzzanRepo
import com.omaraboesmail.logic.utils.getFormattedTime
import com.omaraboesmail.logic.utils.getNextPray
import com.omaraboesmail.logic.utils.getPassedAzzanList
import com.omaraboesmail.logic.utils.getTimeRemainingInMiles
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(val azzanRepo: AzzanRepo) : ViewModel() {

    private val apiResponseData = MutableLiveData<Data>()
    private val query = mutableStateOf(AzzanTimesQuery(""))
    fun getAzzanTimes(): LiveData<Data> {
        viewModelScope.launch {
            query.value.let {
                try {
                    azzanRepo.getAzzanTimes(it).apply {
                        apiResponseData.value = data
                    }
                } catch (e: Exception) {

                }

            }


        }
        return apiResponseData
    }

    fun changeQuery(timesQuery: AzzanTimesQuery) {
        query.value = timesQuery
        getAzzanTimes()
    }

    fun getNextPrayTime(times: Timings) = getNextPray(times)
    fun getPassedAzzanTimes(times: Timings) = getPassedAzzanList(times)
    fun remainingInMilesFromString(timeString: String): Long = getTimeRemainingInMiles(timeString)
    fun getFormattedTimeFromMiles(timeinMills: Long): String = getFormattedTime(timeinMills)

}