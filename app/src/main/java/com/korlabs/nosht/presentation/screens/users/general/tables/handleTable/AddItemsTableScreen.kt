package com.korlabs.nosht.presentation.screens.users.general.tables.handleTable

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.model.MenusWithAmountInOrder
import com.korlabs.nosht.domain.model.ResourceWithAmountInMenu
import com.korlabs.nosht.domain.model.enums.MenuStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.domain.model.enums.TypeTableItemsEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.menu.MenuExtendItem
import com.korlabs.nosht.presentation.components.menu.MenuExtendItemToAddOrder
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItem
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItemToAddMenu
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItemToShow
import com.korlabs.nosht.presentation.components.tables.TypeTableItem
import com.korlabs.nosht.presentation.components.text.TextButtonCustom
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.util.Util

@Composable
fun AddItemsTableScreen(
    navHostController: NavHostController,
    menuViewModel: MenuViewModel,
    resourceViewModel: ResourceViewModel,
    ordersViewModel: OrdersViewModel,
    args: Screen.AddItemsTableScreen
) {
    val context = LocalContext.current

    val menuState = menuViewModel.state
    val resourceState = resourceViewModel.state

    var showDialogAdd by remember { mutableStateOf(false) }
    var addTable by remember { mutableStateOf(false) }
    var processAddTable by remember { mutableStateOf(false) }

    var nameTable by remember { mutableStateOf("") }

    val tableName = args.tableName
    val status = args.status

    var showDialogMenu by remember { mutableStateOf(false) }

    var createOrder by remember { mutableStateOf(false) }
    var amount by remember { mutableFloatStateOf(0f) }

    var currentMenu: Menu? by remember { mutableStateOf(null) }
    var currentMenuToAdd: Menu? by remember { mutableStateOf(null) }

    var selectedTypeTableItem by rememberSaveable { mutableStateOf(TypeTableItemsEnum.ALL) }
    val listMenusSelected = remember { mutableStateListOf<MenusWithAmountInOrder>() }
    val listResourcesSelected = remember { mutableStateListOf<ResourceWithAmountInMenu>() }

    val listTypeTableItemEnum = listOf(
        TypeTableItemsEnum.ALL,
        TypeTableItemsEnum.DYNAMIC_MENU,
        TypeTableItemsEnum.STATIC_MENU,
        TypeTableItemsEnum.COMMERCIAL_PRODUCTS
    )

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.orders_to_table, tableName),
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
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp)
            ) {
                items(listTypeTableItemEnum.size) {
                    TypeTableItem(
                        listTypeTableItemEnum[it],
                        listTypeTableItemEnum[it] == selectedTypeTableItem,
                        { tableItem ->
                            selectedTypeTableItem = tableItem
                        }, true
                    )
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
                        items(listMenusSelected.size) {
                            val currentMenuSelectedInOrderToPaint = listMenusSelected[it]
                            amount = currentMenuSelectedInOrderToPaint.amount.toFloat()

                            MenuExtendItemToAddOrder(
                                currentMenuSelectedInOrderToPaint.menu, amount,
                                { menuSelected ->
                                    for (i in listMenusSelected) {
                                        if (i.menu == menuSelected) {
                                            if (i.menu.menuStatusEnum == MenuStatusEnum.AVAILABLE) {
                                                i.amount = (i.amount + 1).toShort()
                                                amount = i.amount.toFloat()
                                                Log.d(
                                                    Util.TAG,
                                                    "Current amount of ${i.menu.name}: $amount"
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Ya no hay más",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }, { menuSelected ->
                                    for (i in listMenusSelected) {
                                        if (i.menu == menuSelected) {
                                            if (i.amount > 0) {
                                                i.amount = (i.amount - 1).toShort()
                                                amount = i.amount.toFloat()
                                                if (amount.toInt() == 0) {
                                                    listMenusSelected.remove(
                                                        currentMenuSelectedInOrderToPaint
                                                    )
                                                }
                                            }
                                        }
                                    }
                                })
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                        items(listResourcesSelected.size) {
                            val currentResourceWithAmount = listResourcesSelected[it]
                            amount = currentResourceWithAmount.amount
                            ResourceExtendItemToAddMenu(
                                currentResourceWithAmount.resourceBusiness,
                                false,
                                amount,
                                { resourceBusiness ->
                                    for (i in listResourcesSelected) {
                                        if (i.resourceBusiness == resourceBusiness) {
                                            if (i.amount <= resourceBusiness.amount) {
                                                i.amount += 1
                                                amount = i.amount
                                                Log.d(
                                                    Util.TAG,
                                                    "Current amount of ${i.resourceBusiness.name}: $amount"
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Ya no hay más",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }, { resourceBusiness ->
                                    for (i in listResourcesSelected) {
                                        if (i.resourceBusiness == resourceBusiness) {
                                            if (i.amount > 0) {
                                                i.amount -= 1
                                                amount = i.amount
                                                if (amount.toInt() == 0) {
                                                    listResourcesSelected.remove(
                                                        currentResourceWithAmount
                                                    )
                                                }
                                            }
                                        }
                                    }
                                })
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }

                    TypeTableItemsEnum.DYNAMIC_MENU -> {
                        items(menuState.listMenus.size) {
                            if (menuState.listMenus[it].isDynamic) {
                                MenuExtendItem(
                                    menuState.listMenus[it]
                                ) { menu ->
                                    currentMenu = menu
                                    showDialogAdd = true
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
                                ) { menu ->
                                    currentMenu = menu
                                    showDialogAdd = true
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }

                    TypeTableItemsEnum.COMMERCIAL_PRODUCTS -> {
                        items(resourceState.listResourceBusiness.size) {
                            val currentResource = resourceState.listResourceBusiness[it]
                            var currentResourceSaved: ResourceWithAmountInMenu? = null

                            if (currentResource.typeResourceEnum == TypeResourceEnum.COMMERCIAL_PRODUCTS) {
                                ResourceExtendItemToAddMenu(
                                    currentResource, false, amount,
                                    { resourceBusiness ->
                                        var isInList = false

                                        for (i in listResourcesSelected) {
                                            if (i.resourceBusiness == resourceBusiness) {
                                                currentResourceSaved = i

                                                if (i.amount <= resourceBusiness.amount) {
                                                    i.amount += 1
                                                    amount = i.amount
                                                    isInList = true
                                                    Log.d(
                                                        Util.TAG,
                                                        "Current amount of ${i.resourceBusiness.name}: $amount"
                                                    )
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Ya no hay más",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }

                                        if (!isInList) {
                                            amount = 1f

                                            currentResourceSaved =
                                                ResourceWithAmountInMenu(resourceBusiness, amount)

                                            listResourcesSelected.add(currentResourceSaved!!)
                                            Log.d(
                                                Util.TAG,
                                                "Current amount of ${resourceBusiness.name}: 1"
                                            )
                                        }
                                    }, { resourceBusiness ->
                                        for (i in listResourcesSelected) {
                                            if (i.resourceBusiness == resourceBusiness) {
                                                if (i.amount > 0) {
                                                    i.amount -= 1
                                                    amount = i.amount
                                                    if (amount.toInt() == 0) {
                                                        listResourcesSelected.remove(
                                                            currentResourceSaved
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    })
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
                onClick = {
                    navHostController.navigateUp()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(20.dp)
            ) {
                TextButtonCustom(subtitle = stringResource(R.string.cancel), isSecondary = true)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = {
                    ordersViewModel.onEvent(
                        OrdersEvent.UpdateItemsToAddAtOrder(
                            listMenusSelected,
                            listResourcesSelected
                        )
                    )
                    Log.d(Util.TAG, "Menus to set $listMenusSelected")
                    Log.d(Util.TAG, "Resources to set $listResourcesSelected")
                    navHostController.navigateUp()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                TextButtonCustom(subtitle = stringResource(R.string.confirm), isSecondary = false)
            }
        }

        if (showDialogAdd) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.add_menu), fontSize = 20.sp)
                },
                text = {
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
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(1f)
                                .padding(5.dp)
                        ) {
                            if (!currentMenu!!.isDynamic) {
                                currentMenuToAdd = currentMenu!!
                                items(currentMenu!!.listResourceBusiness.sortedBy { it.resourceBusiness.typeResourceEnum }.size) {
                                    ResourceExtendItemToShow(resourceBusiness = currentMenu!!.listResourceBusiness[it].resourceBusiness)
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            } else {
                                val listResourceItemsSelected =
                                    mutableStateListOf<ResourceWithAmountInMenu>()
                                items(currentMenu!!.listResourceBusiness.sortedBy { it.resourceBusiness.typeResourceEnum }.size) {
                                    val currentResourceBusiness =
                                        currentMenu!!.listResourceBusiness[it]
                                    ResourceExtendItem(
                                        currentResourceBusiness.resourceBusiness,
                                        listResourceItemsSelected.any { item ->
                                            item.resourceBusiness == currentResourceBusiness.resourceBusiness
                                        }
                                    ) { resourceBusiness ->
                                        if (!listResourceItemsSelected.any { item -> item.resourceBusiness == resourceBusiness }) {
                                            if (listResourceItemsSelected.any { item -> item.resourceBusiness.typeResourceEnum == resourceBusiness.typeResourceEnum }) {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.order_add_menu_limit),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                listResourceItemsSelected.add(
                                                    currentResourceBusiness
                                                )
                                            }
                                        } else {
                                            listResourceItemsSelected.removeAll { item -> item.resourceBusiness == resourceBusiness }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                                currentMenuToAdd = Menu(
                                    name = currentMenu!!.name,
                                    listResourceItemsSelected,
                                    menuStatusEnum = currentMenu!!.menuStatusEnum,
                                    price = currentMenu!!.price,
                                    isDynamic = false,
                                    documentReference = currentMenu!!.documentReference
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            amount = 1f

                            val menuToAdd = MenusWithAmountInOrder(
                                currentMenuToAdd!!,
                                amount.toInt().toShort()
                            )

                            listMenusSelected.add(menuToAdd)

                            showDialogAdd = false
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        TextButtonCustom(
                            subtitle = stringResource(R.string.add_menu),
                            isSecondary = false
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialogAdd = false
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        TextButtonCustom(
                            subtitle = stringResource(id = R.string.cancel),
                            isSecondary = true
                        )
                    }
                }
            )
        }
    }

    LaunchedEffect(key1 = addTable, block = {
        if (addTable) {
            if (nameTable.isNotBlank()) {
                //tablesViewModel.onEvent(TablesEvent.Add(Table(nameTable)))
                processAddTable = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
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

