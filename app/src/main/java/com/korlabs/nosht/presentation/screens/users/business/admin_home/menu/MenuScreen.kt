package com.korlabs.nosht.presentation.screens.users.business.admin_home.menu

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.menu.MenuExtendItem
import com.korlabs.nosht.presentation.components.menu.MenuItem
import com.korlabs.nosht.presentation.components.tables.TableExtendItem
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util

@Composable
fun MenuScreen(
    navHostController: NavHostController,
    menuViewModel: MenuViewModel
) {
    val context = LocalContext.current

    val state = menuViewModel.state

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.menu_title),
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
                hint = stringResource(id = R.string.enter_menu_name)
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(5.dp)
            ) {
                items(state.listMenus.size) {
                    MenuExtendItem(
                        state.listMenus[it]
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

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { navHostController.navigate(Screen.AddMenuScreen(false)) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.add_static_menu),
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = { navHostController.navigate(Screen.AddMenuScreen(true)) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.add_dynamic_menu),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
            }
        }
    }

    LaunchedEffect(key1 = state.isNewRemoteMenus) {
        if (state.isNewRemoteMenus) {
            Log.d(Util.TAG, "There are a new remote data menus")
            menuViewModel.onEvent(MenuEvent.GetLocalMenus)
        }
    }

    LaunchedEffect(Unit) {
        menuViewModel.onEvent(MenuEvent.GetLocalMenus)
    }

    BackHandler {
        navHostController.navigateUp()
    }
}

