package com.korlabs.nosht.presentation.screens.users.business.admin_home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.users.Business
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navHostController.navigate(Screen.ProfileScreen) }
            )

            Text(
                text = (AuthRepositoryImpl.currentUser as Business).businessName!!,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { /* TO DO - Implement notification screen */ }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(
                id = R.string.message_hello,
                (AuthRepositoryImpl.currentUser as Business).name!!
            ),
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(5.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground,
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
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .clickable {
                        navHostController.navigate(Screen.TablesScreen)
                    }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "$ 0",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .clickable {
                        navHostController.navigate(Screen.TablesScreen)
                    }
            )
        }

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
                text = stringResource(id = R.string.tables_title),
                color = MaterialTheme.colorScheme.onSurface,
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
