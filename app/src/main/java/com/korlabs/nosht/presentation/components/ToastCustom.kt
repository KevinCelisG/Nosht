package com.cohesion.smartwallet.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ToastCustom(text: String) {
    var visible by remember { mutableStateOf(true) }

    if (visible) {
        LaunchedEffect(true) {
            delay(3000)
            visible = false
        }

        Snackbar(
            modifier = Modifier.padding(16.dp),
            content = {
                Text(text)
            }
        )
    }
}

@Preview
@Composable
fun PreviewToastCustom() {
    ToastCustom("Just an example")
}