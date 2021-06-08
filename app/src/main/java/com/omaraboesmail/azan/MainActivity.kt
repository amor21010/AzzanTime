package com.omaraboesmail.azan

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
    val query =
        mutableStateOf("cairo")

    private lateinit var countDownTimer: CountDownTimer
    private val responceData = mutableStateOf<Data?>(null)

    private val viewModel: MainViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            AzzanTheme {
                Surface {
                    CreateView(data = responceData.value)

                }
            }
        }


    }

    @Composable
    private fun CreateTimesView(data: Data) {
        val times = data.timings

        Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {

            val cityIcon =
                painterResource(id = R.drawable.ic_city)
            val city = TextRow(
                cityIcon,
                string = "city : ",
                value = query.value,
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

//times
            val passedAzzanTimes = viewModel.getPassedAzzanTimes(times)
            CreateAzzanTimesCards(timings = times.getTimingAsList(), passedAzaan = passedAzzanTimes)

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
            viewModel.getAzzanTimes().observe(this@MainActivity) { data ->
                responceData.value = data
            }
            Column {


                CreateBody {
                    Button(
                        modifier = Modifier.padding(3.dp),
                        onClick = { viewModel.changeQuery(query.value) }) {
                        Text(text = "Go")
                    }

                }
                if (data != null) {
                    AnimatedVisibility(visible = true) { CreateTimesView(data = data) }
                }
            }

        }
    }

     @Composable
     private  fun CreateBody(function: @Composable () -> Unit) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Card(Modifier.padding(4.dp)) {
                Column(Modifier.padding(4.dp)) {
                    val selectedButton = rememberSaveable {
                        query
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        RadioButton(selected = selectedButton.value.equals("cairo"), onClick = {
                            selectedButton.value = "cairo"
                        })
                        Text(
                            text = "Cairo",
                            Modifier
                                .padding(end = 10.dp)
                                .clickable { selectedButton.value = "cairo" })


                        RadioButton(

                            selected = selectedButton.value.equals("alexandria"),
                            onClick = {
                                selectedButton.value = "alexandria"
                            })
                        Text(
                            text = "Alexandria",
                            Modifier
                                .padding(end = 20.dp)
                                .clickable { selectedButton.value = "alexandria" })
                        function()
                    }
                 /*   Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                    RadioButton(selected = selectedButton.value.equals("MyLocation"), onClick = {
//                        selectedButton.value = "MyLocation"
//                    })
//                    Text(text = "My Location",Modifier.padding(4.dp))

                    }*/


                }
            }
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


