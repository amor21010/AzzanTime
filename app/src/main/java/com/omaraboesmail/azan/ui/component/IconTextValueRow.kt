package com.omaraboesmail.azan.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun CreateTextRow(textRow: TextRow, modifier: Modifier = Modifier, width: Float = .4f) {
    Row(modifier = modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
        textRow.icon?.let {
            Icon(
                painter = it,
                contentDescription = textRow.string,
                modifier = Modifier
                    .height(14.dp)
                    .padding(0.dp, 0.dp, 4.dp, 0.dp)
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(width),
            text = textRow.string,
            style = textRow.textStyle
        )
        textRow.value?.let { value ->
            Text(text = value, style = textRow.valueTextStyle)
        }
    }
}

data class TextRow(
    val icon: Painter? = null,
    val string: String,
    var value: String?,
    val textStyle: TextStyle,
    val valueTextStyle: TextStyle
)