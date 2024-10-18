package com.korlabs.nosht.presentation.screens.users.general.tables.handleTable

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.TableStatusEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.tables.TableExtendItem
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util

@Composable
fun HandleTableScreen(
    navHostController: NavHostController,
    tablesViewModel: TablesViewModel,
    args: Screen.HandleTableScreen
) {
    val context = LocalContext.current

    val state = tablesViewModel.state

    var showDialog by remember { mutableStateOf(false) }
    var addTable by remember { mutableStateOf(false) }
    var processAddTable by remember { mutableStateOf(false) }

    var nameTable by remember { mutableStateOf("") }

    val tableName = args.tableName
    val status = args.status

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
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
                    colorResource(R.color.light_gray),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = "Items",
                color = Color.White,
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
                items(state.listTables.size) {
                    //TableExtendItem((state.listTables[it]))
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(text = "Agregar nuevo cliente") {
            //showDialog = true
            navHostController.navigate(
                Screen.AddItemsTableScreen(
                    tableName,
                    status
                )
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = "Agregar nuevo cliente", fontSize = 20.sp)
                },
                text = {
                    TextFieldCustom(
                        value = nameTable,
                        onValueChange = { nameTable = it },
                        hint = "Ingresa nombre"
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            addTable = true
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

    LaunchedEffect(key1 = state.table, block = {
        if (processAddTable) {
            if (state.table != null) {
                Toast.makeText(context, "Mesa agregada exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error adding table", Toast.LENGTH_SHORT).show()
            }
            showDialog = false
            processAddTable = false
        }
    })

    LaunchedEffect(key1 = state.isNewRemoteTables) {
        if (state.isNewRemoteTables) {
            Log.d(Util.TAG, "There are a new remote data")
            //tablesViewModel.onEvent(TablesEvent.GetLocalTables)
        }
    }

    LaunchedEffect(Unit) {
        //tablesViewModel.onEvent(TablesEvent.GetLocalTables)
    }

    BackHandler {
        navHostController.navigateUp()
    }
}

