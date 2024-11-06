package com.korlabs.nosht.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.korlabs.nosht.presentation.screens.auth.login.LoginScreen
import com.korlabs.nosht.presentation.screens.auth.login.LoginViewModel
import com.korlabs.nosht.presentation.screens.auth.sign_up.SignUpScreen
import com.korlabs.nosht.presentation.screens.auth.splash.SplashScreen
import com.korlabs.nosht.presentation.screens.users.business.admin_home.AdminHomeScreen
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.admin_add_menu.AddMenuScreen
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourceViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.resources.ResourcesScreen
import com.korlabs.nosht.presentation.screens.users.business.admin_manage_employers.AdminManageEmployersScreen
import com.korlabs.nosht.presentation.screens.users.business.admin_manage_employers.EmployersViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_manage_money.AdminManageMoneyScreen
import com.korlabs.nosht.presentation.screens.users.general.profile.ProfileScreen
import com.korlabs.nosht.presentation.screens.users.employers.employer_home.EmployerHomeScreen
import com.korlabs.nosht.presentation.screens.users.employers.waiter.WaiterHomeScreen
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsViewModel
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuScreen
import com.korlabs.nosht.presentation.screens.users.business.admin_home.menu.MenuViewModel
import com.korlabs.nosht.presentation.screens.users.general.orders.OrdersScreen
import com.korlabs.nosht.presentation.screens.users.general.profile.ProfileViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesScreen
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.AddItemsTableScreen
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.HandleTableScreen
import com.korlabs.nosht.presentation.screens.users.general.tables.handleTable.OrdersViewModel
import com.korlabs.nosht.presentation.screens.users.general.timer.TimerViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navHostController: NavHostController
) {
    val tablesViewModel: TablesViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val employersViewModel: EmployersViewModel = hiltViewModel()
    val timerViewModel: TimerViewModel = hiltViewModel()
    val contractsViewModel: ContractsViewModel = hiltViewModel()
    val resourceViewModel: ResourceViewModel = hiltViewModel()
    val menuViewModel: MenuViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val ordersViewModel: OrdersViewModel = hiltViewModel()

    NavHost(
        navController = navHostController,
        startDestination = Screen.SplashScreen
    ) {
        // Auth
        composable<Screen.SplashScreen> {
            SplashScreen(
                navHostController,
                loginViewModel,
                tablesViewModel,
                contractsViewModel,
                resourceViewModel,
                menuViewModel,
                ordersViewModel
            )
        }

        composable<Screen.LoginScreen> {
            LoginScreen(
                navHostController,
                loginViewModel,
                tablesViewModel,
                contractsViewModel,
                resourceViewModel,
                menuViewModel,
                ordersViewModel
            )
        }

        composable<Screen.SignUpScreen> {
            val args = it.toRoute<Screen.SignUpScreen>()
            SignUpScreen(navHostController, args)
        }

        // Admin
        composable<Screen.AdminHomeScreen> {
            AdminHomeScreen(navHostController, tablesViewModel, resourceViewModel, menuViewModel)
        }

        composable<Screen.AdminManageMoneyScreen> {
            AdminManageMoneyScreen()
        }

        composable<Screen.AdminManageEmployersScreen> {
            AdminManageEmployersScreen(navHostController, contractsViewModel, timerViewModel)
        }

        composable<Screen.ProfileScreen> {
            ProfileScreen(profileViewModel)
        }

        composable<Screen.ResourcesScreen> {
            ResourcesScreen(navHostController, resourceViewModel)
        }

        composable<Screen.TablesScreen> {
            TablesScreen(navHostController, tablesViewModel)
        }

        // General employers
        composable<Screen.EmployerHomeScreen> {
            EmployerHomeScreen(
                navHostController,
                tablesViewModel,
                ordersViewModel,
                contractsViewModel
            )
        }

        // Waiters
        composable<Screen.WaiterScreen> {
            WaiterHomeScreen(navHostController, tablesViewModel)
        }

        composable<Screen.HandleTableScreen> {
            val args = it.toRoute<Screen.HandleTableScreen>()
            HandleTableScreen(navHostController, ordersViewModel, args)
        }

        composable<Screen.AddItemsTableScreen> {
            val args = it.toRoute<Screen.AddItemsTableScreen>()
            AddItemsTableScreen(
                navHostController,
                menuViewModel,
                resourceViewModel,
                ordersViewModel,
                args
            )
        }

        composable<Screen.OrdersScreen> {
            val args = it.toRoute<Screen.OrdersScreen>()
            OrdersScreen(navHostController, ordersViewModel, args)
        }

        composable<Screen.MenuScreen> {
            MenuScreen(navHostController, menuViewModel)
        }

        composable<Screen.AddMenuScreen> {
            val args = it.toRoute<Screen.AddMenuScreen>()
            AddMenuScreen(navHostController, resourceViewModel, menuViewModel, args)
        }
    }
}

/*composable<Screen.ScreenB> {
    val args = it.toRoute<Screen.ScreenB>()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${args.name}, ${args.age} years old")

        Button(onClick = {
            currentScreen = Screen.ScreenA

            navController.navigate(
                Screen.ScreenA
            )
        }) {
            Text(text = "Go to screen A")
        }
    }
}*/