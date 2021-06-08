package com.omaraboesmail.bargaincompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.omaraboesmail.azan.ui.theme.Typography

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)


private val LightColorPalette = lightColors(
    primary = WhiteMain,
    onPrimary = BlueDark,
    primaryVariant = BlueMain,
    secondary = BlueDark,
    secondaryVariant = BlueDark,
    surface = WhiteMain,
    background = GreyMain,
    error = Color.Red,
    onError = WhiteMain
)


@Composable
fun AzzanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}