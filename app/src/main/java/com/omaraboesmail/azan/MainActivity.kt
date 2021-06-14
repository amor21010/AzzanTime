package com.omaraboesmail.azan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.omaraboesmail.azan.ui.MainViewModel
import com.omaraboesmail.azan.ui.component.CreateAzzanTimesCards
import com.omaraboesmail.azan.ui.component.CreateRadioButton
import com.omaraboesmail.azan.ui.component.CreateTextRow
import com.omaraboesmail.azan.ui.component.TextRow
import com.omaraboesmail.azan.ui.theme.Typography
import com.omaraboesmail.bargaincompose.ui.theme.AzzanTheme
import com.omaraboesmail.entities.AzzanTimesQuery
import com.omaraboesmail.entities.Data
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val remainingTime =
        mutableStateOf("00:00")
    var mLocationManager: LocationManager? = null

    private val query =
        mutableStateOf(AzzanTimesQuery(city = "cairo"))
    private val remainingTimeString = mutableStateOf("")
    private lateinit var locationProvider: FusedLocationProviderClient

    private val timeInMillis = mutableStateOf(0L)
    private lateinit var countDownTimer: CountDownTimer
    private val responseData = mutableStateOf<Data?>(null)

    private val viewModel: MainViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            AzzanTheme {
                Surface {
                    CreateView(data = responseData.value)

                }
            }
        }


    }


    @ExperimentalAnimationApi
    @Composable
    fun CreateView(data: Data?) {

        Column(
            modifier = Modifier
                .padding(6.dp, 25.dp, 6.dp, 14.dp)
                .fillMaxSize()
        ) {

            //header
            Text(
                text = "Azzan Times",
                style = Typography.h3,
            )
            //body

            Column {

                CreateBody()

                if (data != null) {
                    AnimatedVisibility(visible = true) { CreateInfoView(data = data) }
                }
            }
            observeData()

        }
    }

    private fun observeData() {
        viewModel.getAzzanTimes().observe(this@MainActivity) { data ->
            if (responseData.value != data) {
                responseData.value = data
                val times = data.timings
                remainingTimeString.value = viewModel.getNextPrayTime(times).split("/")[1]
                timeInMillis.value =
                    viewModel.remainingInMilesFromString(timeString = remainingTimeString.value)
                if (this::countDownTimer.isInitialized)
                    countDownTimer.cancel()

                countDownTimer = object : CountDownTimer(timeInMillis.value, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeInMillis.value = millisUntilFinished
                        remainingTime.value =
                            viewModel.getFormattedTimeFromMiles(timeinMills = timeInMillis.value)
                    }

                    override fun onFinish() {
                        remainingTime.value = "00:00"
                        observeData()
                    }
                }

                countDownTimer.start()
            }
        }
    }

    @Composable
    private fun CreateBody() {
        val selectedButton = rememberSaveable {
            mutableStateOf("cairo")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Card(Modifier.padding(4.dp)) {
                Column(Modifier.padding(4.dp)) {


                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        CreateRadioButton(
                            name = "cairo",
                            isSelected = selectedButton.value == "cairo",
                            onclick = { selectedButton.value = "cairo" })


                        CreateRadioButton(
                            name = "alexandria",
                            isSelected = selectedButton.value == "alexandria"
                        ) {
                            selectedButton.value = "alexandria"
                        }

                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CreateRadioButton(
                            name = "My Location",
                            isSelected = selectedButton.value == "My Location"
                        ) {
                            selectedButton.value = "My Location"

                        }

                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                if (selectedButton.value != "My Location") {
                                    query.value.location = null
                                    query.value.timestamp = null
                                    query.value.city = selectedButton.value
                                } else {
                                    query.value.city = null
                                    query.value.location = getLastKnownLocation()

                                }
                                Log.d("TAGoo", "CreateBody: ${query.value}")
                                viewModel.changeQuery(query.value)
                            }


                        ) {
                            Text(text = "Show times")
                        }

                    }
                }


            }
        }


    }

    private fun getLastKnownLocation(): Location? {
        mLocationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        val providers: List<String> = mLocationManager!!.getProviders(true)
        var bestLocation: Location? = null
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1500
            )
        }
        for (provider in providers) {

            val l = mLocationManager!!.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
    }


    @Composable
    private fun CreateInfoView(data: Data) {
        val times = data.timings

        Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {

            val cityIcon =
                painterResource(id = R.drawable.ic_city)
            val city = TextRow(
                cityIcon,
                string = "location : ",
                value = query.value.city
                    ?: try {
                        "(${
                            query.value.location?.latitude.toString().substring(0, 7)
                        } , ${query.value.location?.longitude.toString().substring(0, 7)})"
                    } catch (e: Exception) {
                        "(${
                            query.value.location?.latitude
                        } , ${query.value.location?.longitude})"
                    },
                textStyle = Typography.body2,
                valueTextStyle = Typography.subtitle2
            )

            CreateTextRow(textRow = city)

//gregorian
            val calendarIcon =
                painterResource(id = R.drawable.ic_calendar_interface_symbol_tool)
            val date = TextRow(
                calendarIcon,
                string = "current Date : ",
                value = data.date.gregorian.date,
                textStyle = Typography.body2,
                valueTextStyle = Typography.subtitle2
            )

            CreateTextRow(textRow = date)


//hijri
            val moonIcon =
                painterResource(id = R.drawable.ic_crescent_moon_phase_shape_with_two_stars)
            val hijri = TextRow(
                moonIcon,
                string = "current Date (hijri): ",
                value = data.date.hijri.date,
                textStyle = Typography.body2,
                valueTextStyle = Typography.subtitle2
            )
            CreateTextRow(textRow = hijri)
//nextPray
            val nextPray = viewModel.getNextPrayTime(times).split("/")
            val nextPrayName = nextPray[0].replace(" :", "")
            val nextTimeIcon = painterResource(id = R.drawable.ic_wall_clock)
            val nexTimeRow = TextRow(
                nextTimeIcon,
                string = "Next Azzan Time : ",
                value = nextPrayName,
                textStyle = Typography.body2,
                valueTextStyle = Typography.subtitle2
            )
            CreateTextRow(textRow = nexTimeRow)

//remaining

            val remainingIcon = painterResource(id = R.drawable.ic_hourglass)


            val remaining = TextRow(
                remainingIcon,
                string = "Remaining : ",
                value = remainingTime.value,
                textStyle = Typography.body2,
                valueTextStyle = Typography.subtitle2
            )

            CreateTextRow(textRow = remaining)

//times
            val passedAzzanTimes = viewModel.getPassedAzzanTimes(times)
            CreateAzzanTimesCards(timings = times.getTimingAsList(), passedAzaan = passedAzzanTimes)

        }
    }


}


