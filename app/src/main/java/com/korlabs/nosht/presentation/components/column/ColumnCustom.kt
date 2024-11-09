package com.korlabs.nosht.presentation.components.column

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.korlabs.nosht.ui.theme.Dimens
import com.korlabs.nosht.util.Util

@Composable
fun ColumnCustom(content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(Util.widthPercent(percent = 0.05f))
            .background(MaterialTheme.colorScheme.background)
    ) {
        content()
    }
}