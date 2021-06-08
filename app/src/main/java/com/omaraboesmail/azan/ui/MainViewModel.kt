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
import com.omaraboesmail.logic.utils.getNextPray
import com.omaraboesmail.logic.utils.getPassedAzzanList
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(val azzanRepo: AzzanRepo) : ViewModel() {

    private val apiResponseData = MutableLiveData<Data>()
    private val _isLoding = MutableLiveData<Boolean>()


    fun getAzzanTimes(): LiveData<Data> {
        viewModelScope.launch {
            _isLoding.value = true
            azzanRepo.getAzzanTimes("cairo").apply {
                _isLoding.value = false
                apiResponseData.value = data
            }
        }
        return apiResponseData
    }

    fun getNextPrayTime(times: Timings) = getNextPray(times)
    fun getPassedAzzanTimes(times: Timings) = getPassedAzzanList(times)

}