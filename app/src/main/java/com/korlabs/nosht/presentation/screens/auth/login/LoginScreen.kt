package com.korlabs.nosht.presentation.screens.auth.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.components.text_field.PasswordTextFieldCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsEvent
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    tablesViewModel: TablesViewModel,
    contractsViewModel: ContractsViewModel,
    resourceViewModel: ResourceViewModel,
    menuViewModel: MenuViewModel
) {
    val context = LocalContext.current

    val state = loginViewModel.state

    var email by rememberSaveable { mutableStateOf("idk@jdjd.com") }
    var password by rememberSaveable { mutableStateOf("123456789") }

    var checkLogin by rememberSaveable { mutableStateOf(false) }
    var processLogin by rememberSaveable { mutableStateOf(false) }

    ColumnCustom {
        TextTitleCustom(title = stringResource(id = R.string.login_title))

        TextFieldCustom(
            value = email,
            onValueChange = { email = it },
            hint = stringResource(id = R.string.email_hint)
        )

        PasswordTextFieldCustom(
            value = password,
            onValueChange = { password = it },
            hint = stringResource(id = R.string.password_hint)
        )

        TextSubtitleCustom(subtitle = stringResource(id = R.string.forgot_password))

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        ButtonCustom(text = stringResource(id = R.string.login_title)) {
            checkLogin = true
        }

        Spacer(modifier = Modifier.height(10.dp))

        ButtonCustom(text = stringResource(id = R.string.signup_button), isSecondary = true) {
            navController.navigate(Screen.SignUpScreen(true))
        }

        Text(
            text = stringResource(id = R.string.signup_as_employee),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    navController.navigate(Screen.SignUpScreen(false))
                }
        )

        LaunchedEffect(key1 = checkLogin, block = {
            if (checkLogin) {
                if (email.isNotBlank() && password.isNotBlank()) {
                    loginViewModel.onEvent(LoginEvent.Login(email, password))
                    processLogin = true
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.fill_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                checkLogin = false
            }
        })

        LaunchedEffect(key1 = state.isLoading, block = {
            if (!state.isLoading && processLogin) {
                if (state.user != null) {
                    Log.d(Util.TAG, "Current user ${state.user.typeUserEnum.typeUser}")

                    contractsViewModel.onEvent(ContractsEvent.GetRemoteContracts)

                    if (state.user.typeUserEnum == TypeUserEnum.BUSINESS) {
                        resourceViewModel.onEvent(ResourceEvent.GetRemoteResourceBusiness)
                        tablesViewModel.onEvent(TablesEvent.GetRemoteTables)
                        menuViewModel.onEvent(MenuEvent.GetRemoteMenus)
                        navController.navigate(Screen.AdminHomeScreen)
                    } else {
                        navController.navigate(Screen.EmployerHomeScreen)
                    }
                } else {
                    Toast.makeText(
                        context,
                        state.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                processLogin = false
            }
        })

        BackHandler {
            val activity = (context as? Activity)
            activity?.finish()
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    //LoginScreen(navController = NavHostController(context = LocalContext.current))
}