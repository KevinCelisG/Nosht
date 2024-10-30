package com.korlabs.nosht.presentation.screens.users.business.admin_home.resources

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.ResourceMovement
import com.korlabs.nosht.domain.model.enums.TypeMeasurementEnum
import com.korlabs.nosht.domain.model.enums.TypeMovementEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.resourcesBusiness.ResourceExtendHandleItem
import com.korlabs.nosht.presentation.components.text.TextForDialogCustom
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldFloatCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldNumberCustom
import com.korlabs.nosht.util.Util
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesScreen(
    navHostController: NavHostController,
    resourceViewModel: ResourceViewModel
) {
    val context = LocalContext.current

    val state = resourceViewModel.state

    var showDialog by remember { mutableStateOf(false) }
    var showDialogResource by remember { mutableStateOf(false) }
    var showDialogAddResource by remember { mutableStateOf(false) }
    var showDialogUpdateResource by remember { mutableStateOf(false) }
    var showDialogDeleteResource by remember { mutableStateOf(false) }

    var createResource by remember { mutableStateOf(false) }
    var processCreateResource by remember { mutableStateOf(false) }
    var addResource by remember { mutableStateOf(false) }
    var processAddResource by remember { mutableStateOf(false) }
    var updateResource by remember { mutableStateOf(false) }
    var processUpdateResource by remember { mutableStateOf(false) }
    var deleteResource by remember { mutableStateOf(false) }
    var processDeleteResource by remember { mutableStateOf(false) }

    var currentResource: ResourceBusiness? by remember { mutableStateOf(null) }

    var nameResource: String by remember { mutableStateOf("") }
    var pricePurchaseResource: Float by remember { mutableStateOf(0.0f) }
    var priceSellResource: Float by remember { mutableStateOf(0.0f) }
    var minStockResource: Short by remember { mutableStateOf(0) }
    var maxStockResource: Short by remember { mutableStateOf(0) }
    var currentAmountResource: Float by remember { mutableStateOf(0.0f) }
    var amountResourceToAdd: Float by remember { mutableStateOf(0.0f) }

    var expanded by remember { mutableStateOf(false) }
    var selectedTypeResource by remember { mutableStateOf(TypeResourceEnum.FOOD) }
    val types = listOf(
        TypeResourceEnum.FOOD.type,
        TypeResourceEnum.PROTEIN.type,
        TypeResourceEnum.DRINKS.type,
        TypeResourceEnum.COMMERCIAL_PRODUCTS.type,
        TypeResourceEnum.COMPANION.type
    )

    var expandedMeasurementOptions by remember { mutableStateOf(false) }
    var selectedTypeMeasurement by remember { mutableStateOf(TypeMeasurementEnum.KILOGRAM) }
    val typesMeasurement = listOf(
        TypeMeasurementEnum.KILOGRAM.type,
        TypeMeasurementEnum.LITER.type,
        TypeMeasurementEnum.UNIT.type
    )

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.resources_title),
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
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            TextFieldCustom(
                value = "",
                onValueChange = {

                },
                hint = stringResource(id = R.string.enter_resource_name_hint)
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
                    ResourceExtendHandleItem(
                        resourceBusiness = state.listResourceBusiness[it],
                        onClickShow = { resourceBusiness ->
                            currentResource = resourceBusiness
                            showDialogResource = true
                        },
                        onClickAdd = { resourceBusiness ->
                            currentResource = resourceBusiness
                            showDialogAddResource = true
                        },
                        onClickUpdate = { resourceBusiness ->
                            currentResource = resourceBusiness
                            showDialogUpdateResource = true
                        },
                        onClickDelete = { resourceBusiness ->
                            currentResource = resourceBusiness
                            showDialogDeleteResource = true
                        },
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(text = stringResource(id = R.string.add_new_resource)) {
            showDialog = true
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.add_new_resource), fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextFieldCustom(
                            value = nameResource,
                            onValueChange = { nameResource = it },
                            hint = stringResource(id = R.string.enter_resource_name_hint)
                        )

                        TextFieldNumberCustom(
                            value = minStockResource.toString(),
                            onValueChange = {
                                minStockResource = it
                            },
                            hint = stringResource(id = R.string.enter_resource_min_stock_hint)
                        )

                        TextFieldNumberCustom(
                            value = maxStockResource.toString(),
                            onValueChange = {
                                maxStockResource = it
                            },
                            hint = stringResource(id = R.string.enter_resource_max_stock_hint)
                        )

                        TextFieldFloatCustom(
                            value = currentAmountResource.toString(),
                            onValueChange = {
                                currentAmountResource = it.toFloat()
                            },
                            hint = stringResource(id = R.string.enter_resource_amount_hint)
                        )

                        TextFieldFloatCustom(
                            value = priceSellResource.toString(),
                            onValueChange = {
                                priceSellResource = it.toFloat()
                            },
                            hint = stringResource(id = R.string.enter_resource_price_sell_hint)
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
                                label = { Text(stringResource(id = R.string.select_type_of_resource)) },
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

                        ExposedDropdownMenuBox(
                            expanded = expandedMeasurementOptions,
                            onExpandedChange = {
                                expandedMeasurementOptions = !expandedMeasurementOptions
                            }
                        ) {
                            TextField(
                                readOnly = true,
                                value = selectedTypeMeasurement.type,
                                onValueChange = { },
                                label = { Text(stringResource(id = R.string.select_type_of_measurement)) },
                                trailingIcon = {
                                    TrailingIcon(
                                        expanded = expandedMeasurementOptions
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMeasurementOptions,
                                onDismissRequest = { expandedMeasurementOptions = false }
                            ) {
                                typesMeasurement.forEach { typeMeasurement ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = typeMeasurement)
                                        },
                                        onClick = {
                                            selectedTypeMeasurement =
                                                Util.getTypeMeasurement(typeMeasurement)
                                            expandedMeasurementOptions = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { createResource = true },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            stringResource(id = R.string.add_button),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(
                            stringResource(id = R.string.cancel_button),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            )
        }

        if (showDialogResource && currentResource != null) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = currentResource!!.name, fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_min_stock_hint),
                            text = currentResource!!.minStock.toString()
                        )
                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_max_stock_hint),
                            text = currentResource!!.maxStock.toString()
                        )
                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_amount_hint),
                            text = currentResource!!.amount.toString()
                        )
                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_price_purchase_hint),
                            text = currentResource!!.price.toString()
                        )

                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_type_resource_hint),
                            text = currentResource!!.typeResourceEnum.type
                        )

                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_type_measurement_hint),
                            text = currentResource!!.typeMeasurementEnum.type
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showDialogResource = false },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            stringResource(id = R.string.close),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }

        if (showDialogAddResource && currentResource != null) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = currentResource!!.name, fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_price_purchase_hint),
                            text = currentResource!!.price.toString()
                        )

                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_amount_hint),
                            text = currentResource!!.amount.toString()
                        )

                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_min_stock_hint),
                            text = currentResource!!.minStock.toString()
                        )

                        TextForDialogCustom(
                            title = stringResource(id = R.string.resource_max_stock_hint),
                            text = currentResource!!.maxStock.toString()
                        )

                        TextFieldFloatCustom(
                            value = amountResourceToAdd.toString(),
                            onValueChange = {
                                amountResourceToAdd = it
                            },
                            hint = stringResource(id = R.string.enter_resource_amount_to_add_hint)
                        )

                        TextFieldFloatCustom(
                            value = pricePurchaseResource.toString(),
                            onValueChange = {
                                pricePurchaseResource = it
                            },
                            hint = stringResource(id = R.string.enter_resource_price_purchase_hint)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if ((pricePurchaseResource / amountResourceToAdd) > currentResource!!.price) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.message_suggest_update_the_price),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            addResource = true
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            stringResource(id = R.string.add_button),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialogAddResource = false },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(
                            stringResource(id = R.string.cancel_button),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            )
        }

        if (showDialogUpdateResource && currentResource != null) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.update_resource), fontSize = 20.sp)
                },
                text = {
                    Column {
                        //nameResource = currentResource!!.name
                        TextFieldCustom(
                            value = nameResource,
                            onValueChange = { nameResource = it },
                            hint = stringResource(id = R.string.enter_resource_name_hint)
                        )

                        //minStockResource = currentResource!!.minStock
                        TextFieldNumberCustom(
                            value = minStockResource.toString(),
                            onValueChange = {
                                minStockResource = it
                            },
                            hint = stringResource(id = R.string.enter_resource_min_stock_hint)
                        )

                        //maxStockResource = currentResource!!.maxStock
                        TextFieldNumberCustom(
                            value = maxStockResource.toString(),
                            onValueChange = {
                                maxStockResource = it
                            },
                            hint = stringResource(id = R.string.enter_resource_max_stock_hint)
                        )

                        //currentAmountResource = currentResource!!.amount
                        TextFieldFloatCustom(
                            value = currentAmountResource.toString(),
                            onValueChange = {
                                currentAmountResource = it
                            },
                            hint = stringResource(id = R.string.enter_resource_amount_hint)
                        )

                        //priceSellResource = currentResource!!.price
                        TextFieldFloatCustom(
                            value = priceSellResource.toString(),
                            onValueChange = {
                                priceSellResource = it
                            },
                            hint = stringResource(id = R.string.enter_resource_price_sell_hint)
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
                                label = { Text(stringResource(id = R.string.select_type_of_resource)) },
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

                        ExposedDropdownMenuBox(
                            expanded = expandedMeasurementOptions,
                            onExpandedChange = {
                                expandedMeasurementOptions = !expandedMeasurementOptions
                            }
                        ) {
                            TextField(
                                readOnly = true,
                                value = selectedTypeMeasurement.type,
                                onValueChange = { },
                                label = { Text(stringResource(id = R.string.select_type_of_measurement)) },
                                trailingIcon = {
                                    TrailingIcon(
                                        expanded = expandedMeasurementOptions
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMeasurementOptions,
                                onDismissRequest = { expandedMeasurementOptions = false }
                            ) {
                                typesMeasurement.forEach { typeMeasurement ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = typeMeasurement)
                                        },
                                        onClick = {
                                            selectedTypeMeasurement =
                                                Util.getTypeMeasurement(typeMeasurement)
                                            expandedMeasurementOptions = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { updateResource = true },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            stringResource(id = R.string.update_resource),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialogUpdateResource = false },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(
                            stringResource(id = R.string.cancel_button),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            )
        }

        if (showDialogDeleteResource && currentResource != null) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = currentResource!!.name, fontSize = 20.sp)
                },
                text = {
                    Column {
                        TextTitleCustom(
                            title = stringResource(
                                id = R.string.message_to_delete,
                                currentResource!!.name
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { deleteResource = true },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            stringResource(id = R.string.accept),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialogDeleteResource = false },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(
                            stringResource(id = R.string.cancel_button),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            )
        }
    }

    LaunchedEffect(key1 = deleteResource, block = {
        if (deleteResource) {
            resourceViewModel.onEvent(ResourceEvent.Delete(resourceBusiness = currentResource!!))
            processDeleteResource = true
            deleteResource = false
        }
    })

    LaunchedEffect(key1 = state.isLoading, block = {
        if (processDeleteResource && !state.isLoading) {
            if (state.resourceBusiness != null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.resource_deleted_successfully),
                    Toast.LENGTH_SHORT
                ).show()

                showDialogDeleteResource = false
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_deleting_resource),
                    Toast.LENGTH_SHORT
                ).show()
            }
            processDeleteResource = false
        }
    })

    LaunchedEffect(key1 = updateResource, block = {
        if (updateResource) {
            if (currentAmountResource > 0 && priceSellResource > 0) {
                resourceViewModel.onEvent(
                    ResourceEvent.Update(
                        resourceBusiness = currentResource!!.copy(
                            name = if (currentResource!!.name != nameResource) nameResource else currentResource!!.name,
                            minStock = if (currentResource!!.minStock != minStockResource) minStockResource else currentResource!!.minStock,
                            maxStock = if (currentResource!!.maxStock != maxStockResource) maxStockResource else currentResource!!.maxStock,
                            price = if (currentResource!!.price != priceSellResource) priceSellResource else currentResource!!.price,
                            amount = if (currentResource!!.amount != currentAmountResource) currentAmountResource else currentResource!!.amount,
                            typeResourceEnum = if (currentResource!!.typeResourceEnum != selectedTypeResource) selectedTypeResource else currentResource!!.typeResourceEnum,
                            typeMeasurementEnum = if (currentResource!!.typeMeasurementEnum != selectedTypeMeasurement) selectedTypeMeasurement else currentResource!!.typeMeasurementEnum
                        )
                    )
                )
                processUpdateResource = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
            updateResource = false
        }
    })

    LaunchedEffect(key1 = state.resourceBusiness, block = {
        if (processUpdateResource) {
            if (state.resourceBusiness != null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.resource_updated_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_updating_resource),
                    Toast.LENGTH_SHORT
                ).show()
            }
            showDialogUpdateResource = false
            processUpdateResource = false
        }
    })

    LaunchedEffect(key1 = addResource, block = {
        if (addResource) {
            if (amountResourceToAdd > 0 && pricePurchaseResource > 0) {
                resourceViewModel.onEvent(
                    ResourceEvent.Add(
                        resourceBusiness = currentResource!!,
                        resourceMovement = ResourceMovement(
                            LocalDate.now(),
                            amountResourceToAdd,
                            pricePurchaseResource,
                            TypeMovementEnum.PURCHASE
                        )
                    )
                )
                processAddResource = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
            addResource = false
        }
    })

    LaunchedEffect(key1 = state.resourceBusiness, block = {
        if (processAddResource) {
            if (state.resourceBusiness != null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.resource_added_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_adding_resource),
                    Toast.LENGTH_SHORT
                ).show()
            }
            showDialogAddResource = false
            processAddResource = false
        }
    })

    LaunchedEffect(key1 = createResource, block = {
        if (createResource) {
            if (nameResource.isNotBlank()) {
                resourceViewModel.onEvent(
                    ResourceEvent.Create(
                        ResourceBusiness(
                            nameResource,
                            minStockResource,
                            maxStockResource,
                            priceSellResource,
                            currentAmountResource,
                            selectedTypeResource,
                            selectedTypeMeasurement
                        )
                    )
                )
                processCreateResource = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            createResource = false
        }
    })

    LaunchedEffect(key1 = state.resourceBusiness, block = {
        if (processCreateResource) {
            if (state.resourceBusiness != null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.resource_created_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_making_resource),
                    Toast.LENGTH_SHORT
                ).show()
            }
            showDialog = false
            processCreateResource = false
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

