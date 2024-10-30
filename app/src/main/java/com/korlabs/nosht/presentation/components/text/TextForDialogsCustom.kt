package com.korlabs.nosht.presentation.components.text

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextForDialogCustom(title: String, text: String) {
    Spacer(modifier = Modifier.height(10.dp))

    Text(text = title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(5.dp))

    Text(
        text = text,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        color = MaterialTheme.colorScheme.onBackground
    )
}