package com.omaraboesmail.azan.ui

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omaraboesmail.entities.Data
import com.omaraboesmail.entities.Timings
import com.omaraboesmail.logic.AzzanRepo
import com.omaraboesmail.logic.utils.Time
import com.omaraboesmail.logic.utils.difference
import com.omaraboesmail.logic.utils.getNextPray
import com.omaraboesmail.logic.utils.getPassedAzzanList
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainViewModel @ViewModelInject constructor(val azzanRepo: AzzanRepo) : ViewModel() {

    private val apiResponseData = MutableLiveData<Data>()


    fun getAzzanTimes(): LiveData<Data> {
        viewModelScope.launch {
            azzanRepo.getAzzanTimes("cairo").apply {
                apiResponseData.value = data
                Log.d("ressssss", "$this: ")
            }
        }
        return apiResponseData
    }

    fun getNextPrayTime(times: Timings)=getNextPray(times)
    fun getPassedAzzanTimes(times: Timings)= getPassedAzzanList(times)

}