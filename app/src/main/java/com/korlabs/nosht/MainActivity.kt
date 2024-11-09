package com.korlabs.nosht

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.presentation.components.menu.BottomMenu
import com.korlabs.nosht.navigation.NavigationGraph
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.menu.BottomMenuEmployers
import com.korlabs.nosht.ui.theme.NoshtTheme
import com.korlabs.nosht.util.Util
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setLanguage(this)
        setContent {
            NoshtTheme {
                Content()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Content() {
    val navHostController = rememberNavController()

    val currentBackStackEntry: NavBackStackEntry? by navHostController.currentBackStackEntryAsState()
    val currentScreen = currentBackStackEntry?.destination?.route?.split(".")?.last()

    Scaffold(
        bottomBar = {
            if (AuthRepositoryImpl.currentUser != null) {
                if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
                    if (
                        currentScreen == Screen.AdminHomeScreen.name ||
                        currentScreen?.startsWith(Screen.OrdersScreen().name) == true ||
                        currentScreen == Screen.AdminManageEmployersScreen.name ||
                        currentScreen == Screen.ReportsScreen.name ||
                        currentScreen == Screen.ProfileScreen.name
                    ) {
                        Log.d(Util.TAG, "Showing the bottom menu")
                        BottomMenu(
                            navHostController = navHostController,
                            currentScreen = currentScreen
                        )
                    }
                } else {
                    if (
                        currentScreen == Screen.EmployerHomeScreen.name ||
                        currentScreen == Screen.AdminManageEmployersScreen.name ||
                        currentScreen == Screen.ReportsScreen.name ||
                        currentScreen == Screen.ProfileScreen.name
                    ) {
                        BottomMenuEmployers(
                            navHostController = navHostController,
                            currentScreen = currentScreen
                        )
                    }
                }
            }
        }
    ) {
        NavigationGraph(navHostController)
    }
}

fun setLanguage(context: Context) {
    val currentLocale = Locale.getDefault()
    val language = currentLocale.language

    val newLocale = Locale(language)
    Locale.setDefault(newLocale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(newLocale)
}