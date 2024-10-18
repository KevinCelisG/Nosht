package com.korlabs.nosht.presentation.screens.users.business.admin_home.resources

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendItem
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.util.Util

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesScreen(
    navHostController: NavHostController,
    resourceViewModel: ResourceViewModel
) {
    val context = LocalContext.current

    val state = resourceViewModel.state

    var showDialog by remember { mutableStateOf(false) }
    var addResource by remember { mutableStateOf(false) }
    var processAddResource by remember { mutableStateOf(false) }

    var nameResource by remember { mutableStateOf("") }
    var selectedTypeResource by remember { mutableStateOf(TypeResourceEnum.FOOD) }
    var expanded by remember { mutableStateOf(false) }
    val types = listOf(
        TypeResourceEnum.FOOD.type,
        TypeResourceEnum.PROTEIN.type,
        TypeResourceEnum.DRINKS.type,
        TypeResourceEnum.COMMERCIAL_PRODUCTS.type,
        TypeResourceEnum.COMPANION.type
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
            text = "Recursos",
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
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
            Spacer(modifier = Modifier.height(20.dp))

            TextFieldCustom(
                value = "",
                onValueChange = {

                },
                hint = "Ingresa nombre del recurso"
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(5.dp)
            ) {
                items(state.listResourceBusiness.size) {
                    ResourceExtendItem(state.listResourceBusiness[it], false, { resourceBusiness ->
                        TODO()
                    })
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(text = "Agregar nuevo recurso") {
            showDialog = true
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = "Agregar nuevo recurso", fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextFieldCustom(
                            value = nameResource,
                            onValueChange = { nameResource = it },
                            hint = "Ingresa nombre"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                readOnly = true,
                                value = selectedTypeResource.type,
                                onValueChange = { },
                                label = { Text("Select type of resource") },
                                trailingIcon = {
                                    TrailingIcon(
                                        expanded = expanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                types.forEach { typeResource ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = typeResource)
                                        },
                                        onClick = {
                                            selectedTypeResource =
                                                Util.getTypeResource(typeResource)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            addResource = true
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

    LaunchedEffect(key1 = addResource, block = {
        if (addResource) {
            if (nameResource.isNotBlank()) {
                resourceViewModel.onEvent(
                    ResourceEvent.Add(
                        ResourceBusiness(
                            nameResource,
                            selectedTypeResource
                        )
                    )
                )
                processAddResource = true
            } else {
                Toast.makeText(context, "You must to fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            }
            addResource = false
        }
    })

    LaunchedEffect(key1 = state.resourceBusiness, block = {
        if (processAddResource) {
            if (state.resourceBusiness != null) {
                Toast.makeText(context, "Recurso agregado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error adding resource", Toast.LENGTH_SHORT).show()
            }
            showDialog = false
            processAddResource = false
        }
    })

    LaunchedEffect(key1 = state.isNewRemoteResources) {
        if (state.isNewRemoteResources) {
            Log.d(Util.TAG, "There are a new remote data")
            resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
        }
    }

    LaunchedEffect(Unit) {
        resourceViewModel.onEvent(ResourceEvent.GetLocalResourceBusiness)
    }

    BackHandler {
        navHostController.navigateUp()
    }
}

