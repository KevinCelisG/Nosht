package com.korlabs.nosht.presentation.components.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum

@Composable
fun OrderExtendItem(
    order: Order,
    onClick: (Order) -> Unit,
    onClickUpdateStatus: (Order) -> Unit,
) {
    // IMPROVE
    val colorByType = colorResource(
        when (order.status) {
            OrderStatusEnum.PENDING -> R.color.status_pending
            OrderStatusEnum.COOKING -> R.color.status_cooking
            OrderStatusEnum.READY -> R.color.status_ready
            OrderStatusEnum.SERVED -> R.color.status_served
            OrderStatusEnum.PAID -> R.color.status_paid
            OrderStatusEnum.ALL -> R.color.light_gray
        }
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(1f)
            .height(100.dp)
            .background(colorByType, shape = RoundedCornerShape(15.dp))
            .clickable {
                onClick(order)
            }
    ) {
        Text(
            text = order.status.status,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    onClickUpdateStatus(order)
                }
        )

        Text(
            text = order.date.toString(),
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Text(
            text = "Table 1",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )


        Text(
            text = "Mesero: Pedro Castro",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Text(
            text = "Cocinero: Pablo Snachez",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )


        Text(
            text = "Total: 34.000",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}