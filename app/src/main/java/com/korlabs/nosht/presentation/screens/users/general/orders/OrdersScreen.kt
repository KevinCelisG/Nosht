package com.korlabs.nosht.presentation.screens.users.general.orders

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.ResourceWithAmountInMenu
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.header.HeaderComponent
import com.korlabs.nosht.presentation.components.orders.OrderExtendItem
import com.korlabs.nosht.presentation.components.orders.TypeOrdersItem
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.OrdersEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.OrdersViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(
    navHostController: NavHostController,
    ordersViewModel: OrdersViewModel,
    args: Screen.OrdersScreen
) {

    /*
    TO DO

    - Admin can create, cancel and update orders (Can't pass to Cooking to Ready)
    - Waiter can create, cancel and update orders (Can't pass to Cooking to Ready)
    - Chef can update orders only to pass from Cooking to Ready
    */
    val context = LocalContext.current

    val stateOrders = ordersViewModel.state

    var currentOrder: Order? by remember { mutableStateOf(null) }

    var showOrder by remember { mutableStateOf(false) }
    var updateStatusOrder by remember { mutableStateOf(false) }

    var selectedOrderStatus by rememberSaveable { mutableStateOf(OrderStatusEnum.ALL) }
    val listResourceItemsSelected = remember { mutableStateListOf<ResourceWithAmountInMenu>() }

    val listOrderStateEnum = listOf(
        OrderStatusEnum.ALL,
        OrderStatusEnum.PENDING,
        OrderStatusEnum.COOKING,
        OrderStatusEnum.READY,
        OrderStatusEnum.SERVED,
        OrderStatusEnum.PAID
    )

    ColumnCustom {
        HeaderComponent(navHostController, stringResource(R.string.orders_title))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
            ) {
                items(listOrderStateEnum.size) {
                    TypeOrdersItem(
                        listOrderStateEnum[it],
                        listOrderStateEnum[it] == selectedOrderStatus
                    ) { orderStatusItem ->
                        selectedOrderStatus = orderStatusItem
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(5.dp)
            ) {
                items(stateOrders.listOrders.size) {
                    if (stateOrders.listOrders[it].status == selectedOrderStatus || selectedOrderStatus == OrderStatusEnum.ALL) {
                        OrderExtendItem(order = stateOrders.listOrders[it], { order ->
                            currentOrder = order
                            showOrder = true
                        }, { order ->
                            if (order.status == OrderStatusEnum.COOKING && args.isAChef || AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
                                currentOrder = order
                                updateStatusOrder = true
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.do_not_able_to_update_from_cooking_to_ready),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        if (showOrder) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.orders_title), fontSize = 20.sp)
                },
                text = {
                    Column {
                        Text(text = "Status: ${currentOrder!!.status.status}")
                        Text(text = "Date: ${currentOrder!!.date}")
                        Text(text = "Chef: ${currentOrder!!.idChef}")
                        Text(text = "Table: ${currentOrder!!.idTable}")
                        Text(text = "Waiter: ${currentOrder!!.idWaiter}")
                        Text(text = "Total: ${currentOrder!!.total}")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            updateStatusOrder = true
                            showOrder = false
                        }
                    ) {
                        Text(stringResource(id = R.string.update))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showOrder = false
                            //TO DO
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }
    }

    LaunchedEffect(key1 = updateStatusOrder, block = {
        if (updateStatusOrder) {
            if (currentOrder != null) {
                if (currentOrder!!.status != OrderStatusEnum.PAID && currentOrder!!.status != OrderStatusEnum.ALL) {
                    ordersViewModel.onEvent(OrdersEvent.UpdateStatus(currentOrder!!))
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_updating_status_order),
                    Toast.LENGTH_SHORT
                ).show()
            }
            updateStatusOrder = false
        }
    })

    /*LaunchedEffect(key1 = stateOrders.isNewRemoteResources) {
        if (stateResource.isNewRemoteResources) {
            Log.d(Util.TAG, "There are a new remote data")
            resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
        }
    }

    LaunchedEffect(Unit) {
        resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
    }*/

    BackHandler {
        listResourceItemsSelected.clear()
        navHostController.navigateUp()
    }
}