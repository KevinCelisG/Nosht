package com.korlabs.nosht.presentation.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.TableStatusEnum
import com.korlabs.nosht.domain.model.enums.TimeEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.spacers.SpacerVertical

@Composable
fun DashboardItem(
    item: String,
    context: Context,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageVector = when (item) {
        context.getString(R.string.tables_title) -> {
            Icons.AutoMirrored.Filled.List
        }

        context.getString(R.string.menu_title) -> {
            Icons.Default.Menu
        }

        context.getString(R.string.resources_title) -> {
            Icons.Default.Place
        }

        context.getString(R.string.employers_title) -> {
            Icons.Default.AccountCircle
        }

        else -> {
            Icons.AutoMirrored.Filled.List
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .width(125.dp)
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp))
            .padding(10.dp)
            .clickable { onClick(item) }
    ) {
        Icon(imageVector = imageVector, contentDescription = "IconInDashboard")

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = item,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}