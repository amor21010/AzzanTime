package com.omaraboesmail.azan.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    LazyColumn {
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

@Composable
fun queryCard(onSubmiteClicked: () -> Unit): MutableState<String> {
    val selectedButton = rememberSaveable {
        mutableStateOf("cairo")
    }
    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(4.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                RadioButton(selected = selectedButton.value == "cairo", onClick = {
                    selectedButton.value = "cairo"
                })
                Text(
                    text = "Cairo",
                    Modifier
                        .padding(end = 10.dp)
                        .clickable { selectedButton.value = "cairo" })


                RadioButton(

                    selected = selectedButton.value == "alexandria",
                    onClick = {
                        selectedButton.value = "alexandria"
                    })
                Text(
                    text = "Alexandria",
                    Modifier
                        .padding(end = 20.dp)
                        .clickable { selectedButton.value = "alexandria" })
                Button(
                    modifier = Modifier.padding(3.dp),
                    onClick = onSubmiteClicked
                ) {
                    Text(text = "Show times")
                }


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
    return selectedButton
}

