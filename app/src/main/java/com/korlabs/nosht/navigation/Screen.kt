package com.korlabs.nosht.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val name: String) {

    // Splash
    @Serializable
    data object SplashScreen : Screen("SplashScreen")

    // Auth
    @Serializable
    data object LoginScreen : Screen("LoginScreen")

    @Serializable
    data class SignUpScreen(val isBusiness: Boolean) : Screen("SignUpScreen")

    // General Employers
    @Serializable
    data object EmployerHomeScreen : Screen("EmployerHomeScreen")

    // Features Admin
    @Serializable
    data object AdminHomeScreen : Screen("AdminHomeScreen")

    @Serializable
    data object AdminManageMoneyScreen : Screen("AdminManageMoneyScreen")

    @Serializable
    data object AdminManageEmployersScreen : Screen("AdminManageEmployersScreen")

    @Serializable
    data object ProfileScreen : Screen("ProfileScreen")

    @Serializable
    data object ResourcesScreen : Screen("ResourcesScreen")

    // General Features
    @Serializable
    data object TablesScreen : Screen("TablesScreen")

    // Menu
    @Serializable
    data object MenuScreen : Screen("MenuScreen")

    @Serializable
    data class AddMenuScreen(val isDynamic: Boolean) : Screen("AddMenuScreen")

    // Waiter
    @Serializable
    data object WaiterScreen : Screen("WaiterScreen")

    @Serializable
    data class HandleTableScreen(val tableName: String, val status: String) :
        Screen("HandleTableScreen")

    @Serializable
    data class AddItemsTableScreen(val tableName: String, val status: String) :
        Screen("AddItemsTableScreen")
}

/*@Serializable
    data class ScreenB(
        val name: String?,
        val age: Int
    ) : Screen()*/