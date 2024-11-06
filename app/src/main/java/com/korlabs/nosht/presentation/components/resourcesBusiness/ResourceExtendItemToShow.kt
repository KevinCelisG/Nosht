package com.korlabs.nosht.presentation.components.resourcesBusiness

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.TableStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum

@Composable
fun ResourceExtendItemToShow(
    resourceBusiness: ResourceBusiness
) {
    val colorByType = colorResource(
        when (resourceBusiness.typeResourceEnum) {
            TypeResourceEnum.FOOD -> R.color.food_color
            TypeResourceEnum.PROTEIN -> R.color.protein_color
            TypeResourceEnum.DRINKS -> R.color.drinks_color
            TypeResourceEnum.COMMERCIAL_PRODUCTS -> R.color.commercial_products_color
            TypeResourceEnum.COMPANION -> R.color.companion_color
        }
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(1f)
            .height(100.dp)
            .background(colorByType, shape = RoundedCornerShape(15.dp))
    ) {
        Text(
            text = resourceBusiness.name,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(10.dp)
        )

        Text(
            text = resourceBusiness.typeResourceEnum.type,
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