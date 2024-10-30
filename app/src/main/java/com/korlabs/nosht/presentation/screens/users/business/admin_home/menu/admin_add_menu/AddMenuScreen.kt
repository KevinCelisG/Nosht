package com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.admin_add_menu

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.ResourceWithAmountInMenu
import com.korlabs.nosht.domain.model.enums.MenuStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItem
import com.korlabs.nosht.presentation.components.resourcesBusiness.TypeResourceItem
import com.korlabs.nosht.presentation.components.text.TextButtonCustom
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldFloatCustom
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.util.Util

@Composable
fun AddMenuScreen(
    navController: NavController,
    resourceViewModel: ResourceViewModel,
    menuViewModel: MenuViewModel,
    args: Screen.AddMenuScreen
) {
    val isDynamic = args.isDynamic

    val context = LocalContext.current

    val stateMenu = menuViewModel.state
    val stateResource = resourceViewModel.state

    var showDialog by remember { mutableStateOf(false) }
    var showDialogAddResource by remember { mutableStateOf(false) }

    var currentResource: ResourceBusiness? by remember { mutableStateOf(null) }

    var addMenu by remember { mutableStateOf(false) }
    var processAddMenu by remember { mutableStateOf(false) }

    var selectedTypeResource by rememberSaveable { mutableStateOf(TypeResourceEnum.FOOD) }
    val listResourceItemsSelected = remember { mutableStateListOf<ResourceWithAmountInMenu>() }
    var nameMenu by remember { mutableStateOf("") }
    var priceMenu by remember { mutableFloatStateOf(0f) }
    var amountResourceToAdd: Float by remember { mutableStateOf(0.0f) }

    val listTypeResourceEnum = listOf(
        TypeResourceEnum.FOOD,
        TypeResourceEnum.DRINKS,
        TypeResourceEnum.PROTEIN,
        TypeResourceEnum.COMPANION,
        TypeResourceEnum.COMMERCIAL_PRODUCTS
    )

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.menu_title),
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
                items(listTypeResourceEnum.size) {
                    TypeResourceItem(
                        listTypeResourceEnum[it],
                        listTypeResourceEnum[it] == selectedTypeResource
                    ) { resourceItem ->
                        selectedTypeResource = resourceItem
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
                items(stateResource.listResourceBusiness.size) {
                    if (stateResource.listResourceBusiness[it].typeResourceEnum == selectedTypeResource) {
                        ResourceExtendItem(
                            stateResource.listResourceBusiness[it],
                            listResourceItemsSelected.any { item ->
                                item.resourceBusiness == stateResource.listResourceBusiness[it]
                            }
                        ) { resourceBusiness ->
                            if (isDynamic) {
                                if (!listResourceItemsSelected.any { item -> item.resourceBusiness == resourceBusiness }) {
                                    currentResource = resourceBusiness
                                    showDialogAddResource = true
                                } else {
                                    listResourceItemsSelected.removeAll { item -> item.resourceBusiness == resourceBusiness }
                                }
                            } else {
                                if (!listResourceItemsSelected.any { item -> item.resourceBusiness == resourceBusiness }) {
                                    if (listResourceItemsSelected.any { item -> item.resourceBusiness.typeResourceEnum == resourceBusiness.typeResourceEnum }) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.static_menu_limit_warning),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        currentResource = resourceBusiness
                                        showDialogAddResource = true
                                    }
                                } else {
                                    listResourceItemsSelected.removeAll { item -> item.resourceBusiness == resourceBusiness }
                                }
                            }
                        }
                        Log.d(Util.TAG, listResourceItemsSelected.toString())
                    }
                    Spacer(modifier = Modifier.width(10.dp))
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
                    listResourceItemsSelected.clear()
                    navController.navigateUp()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(20.dp)
            ) {
                TextButtonCustom(
                    subtitle = stringResource(id = R.string.cancel),
                    isSecondary = true
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                TextButtonCustom(
                    subtitle = stringResource(R.string.confirm),
                    isSecondary = false
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.add_new_menu), fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextFieldCustom(
                            value = nameMenu,
                            onValueChange = { nameMenu = it },
                            hint = stringResource(id = R.string.enter_menu_name)
                        )

                        OutlinedTextField(
                            value = priceMenu.toString(),
                            onValueChange = { priceMenu = it.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, end = 30.dp),
                            placeholder = {
                                Text(text = stringResource(id = R.string.enter_menu_price))
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            addMenu = true
                        }
                    ) {
                        Text(stringResource(id = R.string.add_menu))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }

        if (showDialogAddResource) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = currentResource!!.name, fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextFieldFloatCustom(
                            value = amountResourceToAdd.toString(),
                            onValueChange = { amountResourceToAdd = it },
                            hint = stringResource(id = R.string.enter_resource_amount_to_add_hint)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (amountResourceToAdd > 1) {
                                if (amountResourceToAdd < currentResource!!.amount) {
                                    showDialogAddResource = false
                                    listResourceItemsSelected.add(
                                        ResourceWithAmountInMenu(
                                            currentResource!!,
                                            amountResourceToAdd
                                        )
                                    )
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.resource_added_successfully),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    amountResourceToAdd = 0f
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.need_to_be_less_or_equal_that_the_current_amount),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.need_to_be_more_than_zero),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.add))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialogAddResource = false }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }
    }

    LaunchedEffect(key1 = addMenu, block = {
        if (addMenu) {
            if (nameMenu.isNotBlank() && priceMenu > 0) {
                menuViewModel.onEvent(
                    MenuEvent.Add(
                        Menu(
                            nameMenu,
                            listResourceItemsSelected,
                            MenuStatusEnum.AVAILABLE,
                            priceMenu,
                            isDynamic
                        )
                    )
                )
                processAddMenu = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
            addMenu = false
        }
    })

    LaunchedEffect(key1 = stateMenu.menu, block = {
        if (processAddMenu) {
            if (stateMenu.menu != null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.menu_added_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_adding_menu),
                    Toast.LENGTH_SHORT
                ).show()
            }
            showDialog = false
            processAddMenu = false
            navController.navigateUp()
        }
    })

    LaunchedEffect(key1 = stateResource.isNewRemoteResources) {
        if (stateResource.isNewRemoteResources) {
            Log.d(Util.TAG, "There are a new remote data")
            resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
        }
    }

    LaunchedEffect(Unit) {
        resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
    }

    BackHandler {
        listResourceItemsSelected.clear()
        navController.navigateUp()
    }
}