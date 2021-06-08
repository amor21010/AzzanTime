package com.omaraboesmail.azan

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.omaraboesmail.azan.ui.MainViewModel
import com.omaraboesmail.azan.ui.component.CreateAzzanTimesCards
import com.omaraboesmail.azan.ui.component.CreateTextRow
import com.omaraboesmail.azan.ui.component.TextRow
import com.omaraboesmail.azan.ui.theme.Typography
import com.omaraboesmail.bargaincompose.ui.theme.AzzanTheme
import com.omaraboesmail.entities.Data
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val remainingTimeState =
        mutableStateOf("00:00")
    private lateinit var countDownTimer: CountDownTimer

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAzzanTimes()

        viewModel.getAzzanTimes().observe(this@MainActivity) { data ->
            setContent {
                AzzanTheme {
                    Surface {
                        CreateView(data)
                    }
                }
            }
        }


    }


    @Composable
    fun CreateView(data: Data) {

        Column(
            modifier = Modifier
                .padding(6.dp, 25.dp, 6.dp, 14.dp)
                .fillMaxSize()
        ) {
            val times = data.timings

            //header
            Text(
                text = "Azzan Times",
                style = Typography.h3,
            )
            //body


            Column(modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 14.dp)) {
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


                val remainingTime = rememberSaveable {
                    remainingTimeState
                }
                val remainingTimeString: String = viewModel.getNextPrayTime(times).split("/")[1]

                val remainingIcon = painterResource(id = R.drawable.ic_hourglass)
                val hourText = remainingTimeString.split(":")[0].toInt()
                val minText = remainingTimeString.split(":")[1].toInt()
                val countTimeHoursInMilles =
                    (hourText * 60 * 60 * 1000).toLong()
                val countTimeMinInMilles = (minText * 60 * 1000).toLong()
                val timeInMillis =
                    rememberSaveable { mutableStateOf(countTimeHoursInMilles + countTimeMinInMilles) }



                countDownTimer = object : CountDownTimer(timeInMillis.value, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                        timeInMillis.value = millisUntilFinished

                        updateCountDownText(timeInMillis.value)
                    }

                    override fun onFinish() {
                        remainingTime.value = "00:00"
                    }
                }


                countDownTimer.start()
                val remaining = TextRow(
                    remainingIcon,
                    string = "Remaining : ",
                    value = remainingTime.value,
                    textStyle = Typography.body2,
                    valueTextStyle = Typography.subtitle2
                )
                CreateTextRow(textRow = remaining)
            }
//times
            val passedAzzanTimes = viewModel.getPassedAzzanTimes(times)
            CreateAzzanTimesCards(timings = times.getTimingAsList(), passedAzaan = passedAzzanTimes)

        }
    }


    private fun updateCountDownText(
        mTimeLeftInMillis: Long,
    ) {
        val hours = (mTimeLeftInMillis / 1000).toInt() / 3600
        val minutes = (mTimeLeftInMillis / 1000 % 3600).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String = if (hours > 0) {
            java.lang.String.format(
                Locale.getDefault(),
                "%d:%02d:%02d", hours, minutes, seconds
            )
        } else {
            java.lang.String.format(
                Locale.getDefault(),
                "%02d:%02d", minutes, seconds
            )
        }
        remainingTimeState.value = timeLeftFormatted
    }

}


