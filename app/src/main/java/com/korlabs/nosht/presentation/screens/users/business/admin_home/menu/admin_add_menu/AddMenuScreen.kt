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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.enums.MenuStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItem
import com.korlabs.nosht.presentation.components.resourcesBusiness.TypeResourceItem
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
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
    var addMenu by remember { mutableStateOf(false) }
    var processAddMenu by remember { mutableStateOf(false) }

    var selectedTypeResource by rememberSaveable { mutableStateOf(TypeResourceEnum.FOOD) }
    val listResourceItemsSelected = remember { mutableStateListOf<ResourceBusiness>() }
    var nameMenu by remember { mutableStateOf("") }
    var priceMenu by remember { mutableFloatStateOf(0f) }

    val listTypeResourceEnum = listOf(
        TypeResourceEnum.FOOD,
        TypeResourceEnum.DRINKS,
        TypeResourceEnum.PROTEIN,
        TypeResourceEnum.COMPANION,
        TypeResourceEnum.COMMERCIAL_PRODUCTS
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
            text = "Agregar menu",
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
                            listResourceItemsSelected.contains(stateResource.listResourceBusiness[it])
                        ) { resourceBusiness ->
                            if (isDynamic) {
                                if (!listResourceItemsSelected.contains(resourceBusiness)) {
                                    listResourceItemsSelected.add(resourceBusiness)
                                } else {
                                    listResourceItemsSelected.remove(resourceBusiness)
                                }
                            } else {
                                if (listResourceItemsSelected.any { resource -> resource.typeResourceEnum == resourceBusiness.typeResourceEnum }) {
                                    Toast.makeText(
                                        context,
                                        "In a static menus can't add more than 1 type element",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    if (!listResourceItemsSelected.contains(resourceBusiness)) {
                                        listResourceItemsSelected.add(resourceBusiness)
                                    } else {
                                        listResourceItemsSelected.remove(resourceBusiness)
                                    }
                                }
                            }
                            Log.d(Util.TAG, listResourceItemsSelected.toString())
                        }
                        Spacer(modifier = Modifier.width(10.dp))
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
                    listResourceItemsSelected.clear()
                    navController.navigateUp()
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = colorResource(id = R.color.dark_red),
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_red))
            ) {
                TextSubtitleCustom(subtitle = "Cancel", fontColor = Color.White)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = { showDialog = true },
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

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = "Agregar nuevo Menu", fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextFieldCustom(
                            value = nameMenu,
                            onValueChange = { nameMenu = it },
                            hint = "Ingresa nombre del menu"
                        )

                        OutlinedTextField(
                            value = priceMenu.toString(),
                            onValueChange = { priceMenu = it.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, end = 30.dp),
                            placeholder = {
                                Text(text = "Ingresa precio del menu")
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
                        Text("Agregar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancelar")
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
                Toast.makeText(context, "You must to fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            }
            addMenu = false
        }
    })

    LaunchedEffect(key1 = stateMenu.menu, block = {
        if (processAddMenu) {
            if (stateMenu.menu != null) {
                Toast.makeText(context, "Menu agregado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error adding menu", Toast.LENGTH_SHORT).show()
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