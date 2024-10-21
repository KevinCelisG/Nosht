package com.korlabs.nosht.presentation.screens.auth.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.screens.auth.login.LoginEvent
import com.korlabs.nosht.presentation.screens.auth.login.LoginViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceEvent
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsEvent
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel,
    tablesViewModel: TablesViewModel,
    contractsViewModel: ContractsViewModel,
    resourceViewModel: ResourceViewModel,
    menuViewModel: MenuViewModel
) {
    val state = loginViewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    LaunchedEffect(key1 = state.isProcessLoggedReady, block = {
        if (state.isProcessLoggedReady) {
            Log.d(Util.TAG, "Is reviewing")

            delay(2000L)

            if (state.isLogged) {
                Log.d(Util.TAG, "Is logged")

                contractsViewModel.onEvent(ContractsEvent.GetRemoteContracts)

                if (state.user!!.typeUserEnum == TypeUserEnum.BUSINESS) {
                    resourceViewModel.onEvent(ResourceEvent.GetRemoteResourceBusiness)
                    tablesViewModel.onEvent(TablesEvent.GetRemoteTables)
                    menuViewModel.onEvent(MenuEvent.GetRemoteMenus)
                    navHostController.navigate(Screen.AdminHomeScreen)
                } else {
                    navHostController.navigate(Screen.EmployerHomeScreen)
                }
            } else {
                Log.d(Util.TAG, "Is not logged")
                navHostController.navigate(Screen.LoginScreen)
            }
        }
    })

    LaunchedEffect(Unit) {
        loginViewModel.onEvent(LoginEvent.IsLoggedEvent)
        Log.d(Util.TAG, "Start reviewing")
    }
}