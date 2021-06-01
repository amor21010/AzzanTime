package com.omaraboesmail.azan

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
        Log.d("TAGsss", "CreateView: $data")
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
                /*  textAlign = TextAlign.Center*/
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
                val nextPrayName = viewModel.getNextPrayTime(times).split("/")[0].replace(" :", "")
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
                val remainingTime = viewModel.getNextPrayTime(times).split("/")[1]
                val remainingIcon = painterResource(id = R.drawable.ic_hourglass)

                val remaining = TextRow(
                    remainingIcon,
                    string = "Remaining : ",
                    value = remainingTime,
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
}