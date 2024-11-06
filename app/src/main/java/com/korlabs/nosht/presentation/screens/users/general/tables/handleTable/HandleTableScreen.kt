package com.korlabs.nosht.presentation.screens.users.general.tables.handleTable

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.model.enums.TableStatusEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.menu.MenuExtendItem
import com.korlabs.nosht.presentation.components.menu.MenuExtendItemToShow
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItemToShow
import com.korlabs.nosht.presentation.components.tables.TableExtendItem
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HandleTableScreen(
    navHostController: NavHostController,
    ordersViewModel: OrdersViewModel,
    args: Screen.HandleTableScreen
) {
    val context = LocalContext.current

    val state = ordersViewModel.state

    var addOrder by remember { mutableStateOf(false) }
    var processAddOrder by remember { mutableStateOf(false) }

    val tableName = args.tableName
    val status = args.status

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = tableName,
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(5.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.items),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(15.dp)
                    .clickable {
                        navHostController.navigate(Screen.TablesScreen)
                    }
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(5.dp)
            ) {
                items(ordersViewModel.listMenuItems.size) {
                    MenuExtendItemToShow(
                        ordersViewModel.listMenuItems[it].menu,
                        ordersViewModel.listMenuItems[it].amount
                    ) {
                        // TO DO - Show the data of menu
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                items(ordersViewModel.listResourceItems.size) {
                    ResourceExtendItemToShow(ordersViewModel.listResourceItems[it].resourceBusiness)
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(text = stringResource(id = R.string.add_at_order)) {
            navHostController.navigate(
                Screen.AddItemsTableScreen(
                    tableName,
                    status
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        ButtonCustom(text = stringResource(id = R.string.send_order), isSecondary = true) {
            addOrder = true
        }
    }

    LaunchedEffect(key1 = addOrder, block = {
        if (addOrder) {

            Log.d(Util.TAG, "Sending order")

            var total = 0f

            for (i in ordersViewModel.listMenuItems) {
                total += i.amount * i.menu.price
            }

            for (i in ordersViewModel.listResourceItems) {
                total += i.amount * i.resourceBusiness.price
            }

            // TO DO - Implement comments

            ordersViewModel.onEvent(
                OrdersEvent.Add(
                    Order(
                        ordersViewModel.listResourceItems,
                        ordersViewModel.listMenuItems,
                        args.id,
                        AuthRepositoryImpl.currentUser!!.uid.toString(),
                        OrderStatusEnum.PENDING,
                        LocalDate.now(),
                        total,
                        ""
                    )
                )
            )

            Log.d(Util.TAG, "Order sent")

            processAddOrder = true
            addOrder = false
        }
    })

    LaunchedEffect(key1 = state.order, block = {
        if (processAddOrder) {
            if (state.order != null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.order_added_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_adding_order),
                    Toast.LENGTH_SHORT
                ).show()
            }
            processAddOrder = false
        }
    })

    LaunchedEffect(key1 = state.isNewRemoteOrders) {
        if (state.isNewRemoteOrders) {
            Log.d(Util.TAG, "There are a new remote data")
            ordersViewModel.onEvent(OrdersEvent.GetLocalOrders)
        }
    }

    LaunchedEffect(Unit) {
        ordersViewModel.onEvent(OrdersEvent.GetLocalOrders)
    }

    BackHandler {
        navHostController.navigateUp()
    }
}

