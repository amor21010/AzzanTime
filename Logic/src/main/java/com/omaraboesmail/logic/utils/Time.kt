package com.omaraboesmail.logic.utils

import android.util.Log
import com.omaraboesmail.entities.Timings
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Time(var hours: Int, var minutes: Int)

fun difference(start: Time, stop: Time): Time {
    val diff = Time(0, 0)

    if (stop.minutes > start.minutes) {
        --start.hours
        start.minutes += 60
    }

    diff.minutes = start.minutes - stop.minutes
    diff.hours = start.hours - stop.hours

    return diff
}


fun getNextPray(azzanTimes: Timings): String {
    val timesDiffs = getTimeDeffs(azzanTimes)
    Log.d("TAGOOOO", "getNextPray: $timesDiffs")
    var nextPray: String = timesDiffs[0]

    timesDiffs.forEach { time ->
        val flagHour = nextPray.split("/")[1].split(":")[0].toInt()
        val flagMin = nextPray.split("/")[1].split(":")[1].toInt()
        if (flagHour < 0) nextPray = time
        else {
            val currentItemHour = time.split("/")[1].split(":")[0].toInt()
            val currentItemMin = time.split("/")[1].split(":")[1].toInt()
            if (currentItemHour > 0) {
                if (flagHour > currentItemHour) nextPray = time
                if (flagHour == currentItemHour && flagMin > currentItemMin) nextPray = time
            }
        }
    }
    return nextPray
}

fun getTimeDeffs(azzanTimes: Timings): List<String> {
    val times = azzanTimes.getTimingAsList()
    var differanceBetweenAzzanAndCurrent: Time
    val timesDiffs = ArrayList<String>()
    times.forEach { azzantime ->
        val hours = azzantime.split("/")[1].split(":")[0].toInt()
        val minutes = azzantime.split("/")[1].split(":")[1].toInt()
        val time = getCurrentTime()
        val currentHour = time.split(":")[0].toInt()
        val currentMinutes = time.split(":")[1].toInt()
        differanceBetweenAzzanAndCurrent = difference(
            Time(hours, minutes),
            Time(currentHour, currentMinutes)
        )
        timesDiffs.add("${azzantime.split("/")[0]} /${differanceBetweenAzzanAndCurrent.hours}:${differanceBetweenAzzanAndCurrent.minutes}")
    }
    return timesDiffs
}


fun getPassedAzzanList(azzanTimes: Timings): List<String> {
    val diffList = getTimeDeffs(azzanTimes)
    val nigativeList = ArrayList<String>()
    diffList.forEach { time ->
        val flagHour = time.split("/")[1].split(":")[0].toInt()
        if (flagHour < 0) nigativeList.add(time)
    }
    return nigativeList
}

fun getCurrentTime(): String {
    val currentDateTime = Calendar.getInstance().time
    return SimpleDateFormat("hh:mm",Locale("En")).format(currentDateTime)
}