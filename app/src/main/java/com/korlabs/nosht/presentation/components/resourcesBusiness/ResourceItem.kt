package com.korlabs.nosht.presentation.components.resourcesBusiness

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum

@Composable
fun ResourceItem(resourceBusiness: ResourceBusiness, modifier: Modifier = Modifier) {
    val color =
        colorResource(
            id = when (resourceBusiness.typeResourceEnum) {
                TypeResourceEnum.FOOD -> R.color.food_color
                TypeResourceEnum.PROTEIN -> R.color.protein_color
                TypeResourceEnum.DRINKS -> R.color.drinks_color
                TypeResourceEnum.COMMERCIAL_PRODUCTS -> R.color.commercial_products_color
                TypeResourceEnum.COMPANION -> R.color.companion_color
            }
        )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(5.dp)
            .width(75.dp)
            .fillMaxHeight(1f)
            .background(color, shape = RoundedCornerShape(15.dp))
    ) {
        Text(
            text = resourceBusiness.name,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}
