package com.korlabs.nosht.presentation.screens.users.business.admin_home

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.DashboardItem
import com.korlabs.nosht.presentation.components.header.HeaderComponent
import com.korlabs.nosht.presentation.components.menu.MenuItem
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceItem
import com.korlabs.nosht.presentation.components.spacers.SpacerVertical
import com.korlabs.nosht.presentation.components.tables.TableItem
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.OrdersEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.OrdersViewModel
import com.korlabs.nosht.util.Util

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminHomeScreen(
    navHostController: NavHostController,
    tablesViewModel: TablesViewModel,
    resourceViewModel: ResourceViewModel,
    ordersViewModel: OrdersViewModel,
    menuViewModel: MenuViewModel
) {
    val context = LocalContext.current

    val stateTables = tablesViewModel.state
    val stateResources = resourceViewModel.state
    val stateMenus = menuViewModel.state
    val stateOrders = ordersViewModel.state

    var currentProfit by remember { mutableStateOf(0.0f) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(Util.widthPercent(percent = 0.05f))
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        HeaderComponent(navHostController)

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(15.dp)
                )
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sales_day),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .clickable {
                        navHostController.navigate(Screen.ReportsScreen)
                    }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "$ $currentProfit",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .clickable {
                        navHostController.navigate(Screen.ReportsScreen)
                    }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        /*Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Text(
                text = stringResource(id = R.string.news),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        navHostController.navigate(Screen.MenuScreen)
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(5.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.newsTest),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            navHostController.navigate(Screen.ReportsScreen)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))*/

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            Text(
                text = stringResource(id = R.string.features),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        navHostController.navigate(Screen.MenuScreen)
                    }
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                item {
                    DashboardItem(
                        item = stringResource(id = R.string.tables_title),
                        context = context,
                        { navHostController.navigate(Screen.TablesScreen) }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                item {
                    DashboardItem(
                        item = stringResource(id = R.string.menu_title),
                        context = context,
                        { navHostController.navigate(Screen.MenuScreen) }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                item {
                    DashboardItem(
                        item = stringResource(id = R.string.resources_title),
                        context = context,
                        { navHostController.navigate(Screen.ResourcesScreen) }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                item {
                    DashboardItem(
                        item = stringResource(id = R.string.employers_title),
                        context = context,
                        { navHostController.navigate(Screen.AdminManageEmployersScreen) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp))
                .clickable { navHostController.navigate(Screen.OrdersScreen()) }
                .padding(10.dp)
        ) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "IconOrders")

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.orders_title),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        /*Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(5.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.menu_title),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(15.dp)
                    .clickable {
                        navHostController.navigate(Screen.MenuScreen)
                    }
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp)
            ) {
                items(stateMenus.listMenus.size) {
                    MenuItem(stateMenus.listMenus[it])
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(5.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.resources_title),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(15.dp)
                    .clickable {
                        navHostController.navigate(Screen.ResourcesScreen)
                    }
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp)
            ) {
                items(stateResources.listResourceBusiness.size) {
                    // Change to menu list
                    ResourceItem(stateResources.listResourceBusiness[it])
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }*/
    }

    LaunchedEffect(key1 = stateOrders.report) {
        Log.d(
            Util.TAG,
            "The report value in the state changed. This is the currently value ${stateOrders.report}"
        )
        if (stateOrders.report != null) {
            Log.d(Util.TAG, "The currentReport now is ${stateOrders.report}")
            currentProfit = stateOrders.report.profit
        }
    }

    LaunchedEffect(key1 = stateTables.isNewRemoteTables) {
        if (stateTables.isNewRemoteTables) {
            Log.d(Util.TAG, "There are a new remote tables data")
            tablesViewModel.onEvent(TablesEvent.GetLocalTables)
        } else {
            Log.d(Util.TAG, "There are NOT a new remote tables data")
        }
    }

    LaunchedEffect(key1 = stateResources.isNewRemoteResources) {
        if (stateResources.isNewRemoteResources) {
            Log.d(Util.TAG, "There are a new remote resource data")
            resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
        }
    }

    LaunchedEffect(key1 = stateMenus.isNewRemoteMenus) {
        if (stateMenus.isNewRemoteMenus) {
            Log.d(Util.TAG, "There are a new remote menus data")
            menuViewModel.onEvent(MenuEvent.GetLocalMenus)
        }
    }

    LaunchedEffect(Unit) {
        tablesViewModel.onEvent(TablesEvent.GetLocalTables)
        resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
        menuViewModel.onEvent(MenuEvent.GetLocalMenus)
        ordersViewModel.onEvent(OrdersEvent.GenerateReport())
    }

    BackHandler {
        FirestoreClient.stopNewData()
        FirestoreClient.stopListeners()

        navHostController.navigate(Screen.LoginScreen)
    }
}
