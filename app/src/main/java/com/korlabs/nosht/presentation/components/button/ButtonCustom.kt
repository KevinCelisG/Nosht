package com.korlabs.nosht.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.korlabs.nosht.R
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom

@Composable
fun ButtonCustom(
    text: String,
    isSecondary: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSecondary) {
        colorResource(id = R.color.dark_red)
    } else {
        colorResource(id = R.color.dark_blue)
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 40.dp,
                end = 40.dp
            )
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor)
    ) {
        TextSubtitleCustom(subtitle = text, fontColor = Color.White)
    }
}