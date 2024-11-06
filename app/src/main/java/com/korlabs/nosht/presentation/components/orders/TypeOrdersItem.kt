package com.korlabs.nosht.presentation.components.orders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum

@Composable
fun TypeOrdersItem(
    orderStatusEnum: OrderStatusEnum,
    isSelected: Boolean,
    onClick: (OrderStatusEnum) -> Unit
) {
    val colorId = colorResource(if (isSelected) R.color.orange else R.color.light_gray)
    val fontColorId = colorResource(if (isSelected) R.color.black else R.color.white)

    Button(
        onClick = {
            onClick(orderStatusEnum)
        },
        modifier = Modifier
            .background(
                color = colorId,
                shape = RoundedCornerShape(20.dp)
            ),
        colors = ButtonDefaults.buttonColors(colorId),
        border = BorderStroke(
            2.dp, colorResource(id = R.color.orange)
        )
    ) {
        Text(
            text = orderStatusEnum.status,
            color = fontColorId,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}