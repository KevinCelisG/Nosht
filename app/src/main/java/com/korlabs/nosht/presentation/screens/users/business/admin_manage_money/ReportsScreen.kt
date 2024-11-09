package com.korlabs.nosht.presentation.screens.users.business.admin_manage_money

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.Report
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.model.enums.TimeEnum
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.header.HeaderComponent
import com.korlabs.nosht.presentation.components.orders.OrderExtendItem
import com.korlabs.nosht.presentation.components.orders.TypeOrdersItem
import com.korlabs.nosht.presentation.components.reports.ReportItem
import com.korlabs.nosht.presentation.components.reports.TimeItem
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.OrdersEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.OrdersViewModel
import com.korlabs.nosht.util.Util

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportsScreen(
    navHostController: NavHostController,
    ordersViewModel: OrdersViewModel
) {
    val context = LocalContext.current

    val stateOrders = ordersViewModel.state
    var currentReport by remember { mutableStateOf(stateOrders.report) }

    var selectedTime by rememberSaveable { mutableStateOf(TimeEnum.TODAY) }
    val listTimeReportEnum = listOf(
        TimeEnum.TODAY,
        TimeEnum.YESTERDAY,
        TimeEnum.LAST_WEEK,
        TimeEnum.LAST_MONTH
    )

    ColumnCustom {
        HeaderComponent(navHostController, stringResource(R.string.reports_title))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
            ) {
                items(listTimeReportEnum.size) {
                    TimeItem(
                        listTimeReportEnum[it],
                        listTimeReportEnum[it] == selectedTime
                    ) { timeItem ->
                        ordersViewModel.onEvent(OrdersEvent.GenerateReport(timeItem))
                        selectedTime = timeItem
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

            ReportItem(
                title = when (selectedTime) {
                    TimeEnum.TODAY -> R.string.sales_today
                    TimeEnum.YESTERDAY -> R.string.sales_yesterday
                    TimeEnum.LAST_WEEK -> R.string.sales_last_week
                    TimeEnum.LAST_MONTH -> R.string.sales_last_month
                },
                data = if (currentReport != null) "$ ${currentReport!!.profit}" else "$ 0.0"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReportItem(
                    title = R.string.total_revenue,
                    data = if (currentReport != null) "$ ${currentReport!!.revenue}" else "$ 0.0",
                    isSmall = true
                )

                ReportItem(
                    title = R.string.total_orders,
                    data = if (currentReport != null) "${currentReport!!.totalOrders}" else "0",
                    isSmall = true
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReportItem(
                    title = R.string.best_waiter,
                    data = if (currentReport != null) {
                        if (currentReport!!.bestWaiter != null) {
                            currentReport!!.bestWaiter!!.key
                        } else {
                            "Nadie"
                        }
                    } else {
                        "Nadie"
                    },
                    isSmall = true
                )

                ReportItem(
                    title = R.string.best_resource,
                    data = if (currentReport != null) {
                        if (currentReport!!.bestResource != null) {
                            currentReport!!.bestResource!!.key.name
                        } else {
                            "Ninguno"
                        }
                    } else {
                        "Ninguno"
                    },
                    isSmall = true
                )
            }

            ReportItem(
                title = R.string.best_menu,
                data =
                if (currentReport != null) {
                    if (currentReport!!.bestMenu != null) {
                        currentReport!!.bestMenu!!.key.name
                    } else {
                        "Ninguno"
                    }
                } else {
                    "Ninguno"
                }
            )
        }
    }

    LaunchedEffect(key1 = stateOrders.report) {
        Log.d(
            Util.TAG,
            "The report value in the state changed. This is the currently value ${stateOrders.report}"
        )
        if (stateOrders.report != null) {
            Log.d(Util.TAG, "The currentReport now is ${stateOrders.report}")
            currentReport = stateOrders.report
        }
    }

    LaunchedEffect(Unit) {
        ordersViewModel.onEvent(OrdersEvent.GenerateReport())
    }

    BackHandler {
        navHostController.navigateUp()
    }
}