package com.korlabs.nosht.presentation.screens.users.general.tables.handleTable

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.domain.model.enums.TypeTableItemsEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.menu.MenuExtendItem
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItem
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceItem
import com.korlabs.nosht.presentation.components.tables.TypeTableItem
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util

@Composable
fun AddItemsTableScreen(
    navHostController: NavHostController,
    tablesViewModel: TablesViewModel,
    menuViewModel: MenuViewModel,
    resourceViewModel: ResourceViewModel,
    args: Screen.AddItemsTableScreen
) {
    val context = LocalContext.current

    val menuState = menuViewModel.state
    val resourceState = resourceViewModel.state

    var showDialog by remember { mutableStateOf(false) }
    var addTable by remember { mutableStateOf(false) }
    var processAddTable by remember { mutableStateOf(false) }

    var nameTable by remember { mutableStateOf("") }

    val tableName = args.tableName
    val status = args.status

    var selectedTypeTableItem by rememberSaveable { mutableStateOf(TypeTableItemsEnum.ALL) }

    val listTypeTableItemEnum = listOf(
        TypeTableItemsEnum.ALL,
        TypeTableItemsEnum.DYNAMIC_MENU,
        TypeTableItemsEnum.STATIC_MENU,
        TypeTableItemsEnum.COMMERCIAL_PRODUCTS
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Orders to $tableName",
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
                    colorResource(R.color.light_gray),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp)
            ) {
                items(listTypeTableItemEnum.size) {
                    TypeTableItem(
                        listTypeTableItemEnum[it],
                        listTypeTableItemEnum[it] == selectedTypeTableItem
                    ) { tableItem ->
                        selectedTypeTableItem = tableItem
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
                when (selectedTypeTableItem) {
                    TypeTableItemsEnum.ALL -> {
                        items(menuState.listMenus.size) {
                            MenuExtendItem(
                                menuState.listMenus[it]
                            ) { /*table ->
                            navHostController.navigate(
                                Screen.HandleTableScreen(
                                    table.name,
                                    table.status.status
                                )
                            )*/
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                        items(resourceState.listResourceBusiness.size) {
                            if (resourceState.listResourceBusiness[it].typeResourceEnum == TypeResourceEnum.COMMERCIAL_PRODUCTS) {
                                ResourceExtendItem(
                                    resourceState.listResourceBusiness[it], false
                                ) { /*table ->
                            navHostController.navigate(
                                Screen.HandleTableScreen(
                                    table.name,
                                    table.status.status
                                )
                            )*/
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }

                    TypeTableItemsEnum.DYNAMIC_MENU -> {
                        items(menuState.listMenus.size) {
                            if (menuState.listMenus[it].isDynamic) {
                                MenuExtendItem(
                                    menuState.listMenus[it]
                                ) { /*table ->
                            navHostController.navigate(
                                Screen.HandleTableScreen(
                                    table.name,
                                    table.status.status
                                )
                            )*/
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }

                    TypeTableItemsEnum.STATIC_MENU -> {
                        items(menuState.listMenus.size) {
                            if (!menuState.listMenus[it].isDynamic) {
                                MenuExtendItem(
                                    menuState.listMenus[it]
                                ) { /*table ->
                            navHostController.navigate(
                                Screen.HandleTableScreen(
                                    table.name,
                                    table.status.status
                                )
                            )*/
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }

                    TypeTableItemsEnum.COMMERCIAL_PRODUCTS -> {
                        items(resourceState.listResourceBusiness.size) {
                            if (resourceState.listResourceBusiness[it].typeResourceEnum == TypeResourceEnum.COMMERCIAL_PRODUCTS) {
                                ResourceExtendItem(
                                    resourceState.listResourceBusiness[it], false
                                ) { /*table ->
                            navHostController.navigate(
                                Screen.HandleTableScreen(
                                    table.name,
                                    table.status.status
                                )
                            )*/
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = colorResource(id = R.color.dark_red),
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_red))
            ) {
                TextSubtitleCustom(subtitle = "Cancelar", fontColor = Color.White)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = colorResource(id = R.color.dark_blue),
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_blue))
            ) {
                TextSubtitleCustom(subtitle = "Confirm", fontColor = Color.White)
            }
        }
    }

    LaunchedEffect(key1 = addTable, block = {
        if (addTable) {
            if (nameTable.isNotBlank()) {
                //tablesViewModel.onEvent(TablesEvent.Add(Table(nameTable)))
                processAddTable = true
            } else {
                Toast.makeText(context, "You must to fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            }
            addTable = false
        }
    })

//    LaunchedEffect(key1 = state.table, block = {
//        if (processAddTable) {
//            if (state.table != null) {
//                Toast.makeText(context, "Table added successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, "Error adding table", Toast.LENGTH_SHORT).show()
//            }
//            showDialog = false
//            processAddTable = false
//        }
//    })

    LaunchedEffect(key1 = menuState.isNewRemoteMenus) {
        if (menuState.isNewRemoteMenus) {
            Log.d(Util.TAG, "There are a new remote data")
            menuViewModel.onEvent(MenuEvent.GetLocalMenus)
        }
    }

    LaunchedEffect(key1 = resourceState.isNewRemoteResources) {
        if (resourceState.isNewRemoteResources) {
            Log.d(Util.TAG, "There are a new remote data")
            resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
        }
    }

    LaunchedEffect(Unit) {
        resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
        menuViewModel.onEvent(MenuEvent.GetLocalMenus)
    }

    BackHandler {
        navHostController.navigateUp()
    }
}

