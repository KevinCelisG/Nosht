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
import com.korlabs.nosht.presentation.components.spacers.SpacerVertical
import com.korlabs.nosht.ui.theme.Dimens
import com.korlabs.nosht.util.Util

@Composable
fun TextTitleCustom(title: String) {
    SpacerVertical(0.02f)

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(Util.heightPercent(percent = Dimens.headerHeight)),
        color = MaterialTheme.colorScheme.onBackground
    )
}