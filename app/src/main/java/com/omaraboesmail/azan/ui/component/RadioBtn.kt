package com.omaraboesmail.azan.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateRadioButton(name: String, isSelected: Boolean, onclick: () -> Unit) {
    RadioButton(
        selected = isSelected, onClick =
        onclick
    )
    Text(
        text = name,
        Modifier
            .padding(end = 10.dp, start = 4.dp)
            .clickable { onclick() })

}