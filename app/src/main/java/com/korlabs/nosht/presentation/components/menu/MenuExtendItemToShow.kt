package com.korlabs.nosht.presentation.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.Menu


@Composable
fun MenuExtendItemToShow(
    menu: Menu,
    amount: Short,
    onClick: (Menu) -> Unit
) {
    // IMPROVE
    val colorByType = colorResource(if (menu.isDynamic) R.color.dark_green else R.color.light_gray)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(1f)
            .height(100.dp)
            .background(colorByType, shape = RoundedCornerShape(15.dp))
            .clickable {
                onClick(menu)
            }
    ) {
        Text(
            text = menu.name,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(10.dp)
        )

        Text(
            text = amount.toString(),
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(10.dp)
        )
    }
}