package com.korlabs.nosht.presentation.components.reports

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.model.enums.TimeEnum
import com.korlabs.nosht.util.Util

@Composable
fun ReportItem(
    title: Int,
    data: String,
    isSmall: Boolean = false
) {
    Column(
        modifier = Modifier
            .width(Util.widthPercent(percent = if (isSmall) 0.45f else 0.9f))
            .height(if (isSmall) 90.dp else 110.dp)
            .padding(5.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(15.dp)
            )
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(15.dp)
    ) {
        Text(
            text = stringResource(title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = if (isSmall) 14.sp else 16.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = data,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = if (isSmall) 16.sp else 18.sp,
            textAlign = TextAlign.Start,
        )
    }
}
