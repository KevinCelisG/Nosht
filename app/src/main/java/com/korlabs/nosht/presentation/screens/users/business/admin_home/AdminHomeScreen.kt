package com.korlabs.nosht.presentation.screens.users.business.admin_home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.menu.MenuItem
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceItem
import com.korlabs.nosht.presentation.components.tables.TableItem
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util

@Composable
fun AdminHomeScreen(
    navHostController: NavHostController,
    tablesViewModel: TablesViewModel,
    resourceViewModel: ResourceViewModel,
    menuViewModel: MenuViewModel
) {
    val context = LocalContext.current

    val stateTables = tablesViewModel.state
    val stateResources = resourceViewModel.state
    val stateMenus = menuViewModel.state

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Dashboard",
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(5.dp)
                .background(
                    colorResource(R.color.light_gray),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = "Mesas",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(15.dp)
                    .clickable {
                        navHostController.navigate(Screen.TablesScreen)
                    }
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp)
            ) {
                items(stateTables.listTables.size) {
                    TableItem(stateTables.listTables[it])
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
                    colorResource(R.color.light_gray),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = "Menus",
                color = Color.White,
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
                    colorResource(R.color.light_gray),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = "Recursos",
                color = Color.White,
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
    }

    BackHandler {
        FirestoreClient.stopNewData()
        FirestoreClient.stopListeners()

        navHostController.navigate(Screen.LoginScreen)
    }
}
