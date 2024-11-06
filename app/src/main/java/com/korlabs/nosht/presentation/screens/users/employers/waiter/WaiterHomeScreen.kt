package com.korlabs.nosht.presentation.screens.users.employers.waiter

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.tables.TableExtendItem
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util

@Composable
fun WaiterHomeScreen(
    navHostController: NavHostController,
    tablesViewModel: TablesViewModel
) {
    val context = LocalContext.current

    val state = tablesViewModel.state

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.tables_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
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
                hint = stringResource(id = R.string.enter_table_name_hint)
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(5.dp)
            ) {
                items(state.listTables.size) {
                    TableExtendItem((state.listTables[it]), { table ->
                        navHostController.navigate(
                            Screen.HandleTableScreen(
                                table.name,
                                table.status.status,
                                table.documentReference.toString()
                            )
                        )
                    })
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }

    LaunchedEffect(key1 = state.isNewRemoteTables) {
        if (state.isNewRemoteTables) {
            Log.d(Util.TAG, "There are a new remote data")
            tablesViewModel.onEvent(TablesEvent.GetLocalTables)
        }
    }

    LaunchedEffect(Unit) {
        tablesViewModel.onEvent(TablesEvent.GetLocalTables)
    }

    BackHandler {
        navHostController.navigateUp()
    }
}

