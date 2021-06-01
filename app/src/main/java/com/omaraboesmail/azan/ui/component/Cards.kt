package com.omaraboesmail.azan.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.omaraboesmail.azan.R
import com.omaraboesmail.azan.ui.theme.Typography

@Composable
fun CreateAzzanTimesCards(timings: List<String>, passedAzaan: List<String>) {
    LazyColumn() {
        Log.d("TAGtttt", "CreateAzzanTimesCards: $passedAzaan")
        itemsIndexed(timings) { index: Int, item: String ->
            TimeCard(AzzanTime = item, index, passedAzaan.size)
        }

    }

}

@Composable
fun TimeCard(AzzanTime: String, index: Int, passedAzaan: Int) {
    Card(
        elevation = 2.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        val timeTextRow = TextRow(
            string = AzzanTime.split("/")[0],
            value = AzzanTime.split("/")[1],
            textStyle = Typography.body2,
            valueTextStyle = Typography.subtitle2
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            val isPassed = rememberSaveable {
                mutableStateOf(true)
            }
            CreateTextRow(textRow = timeTextRow, Modifier.padding(6.dp, 14.dp), .23f)
            val painter =
                painterResource(
                    id =
                    if (index >= passedAzaan)
                        R.drawable.ic_hourglass
                    else
                        R.drawable.ic_kneel_pray
                )
            Icon(
                if (isPassed.value) painter else painter,
                contentDescription = "Passed",
                modifier = Modifier
                    .height(14.dp)
                    .fillMaxWidth(.2f)
            )
            if (index < passedAzaan)
                Text(text = "Passed", Modifier.fillMaxWidth())


        }
    }
}
