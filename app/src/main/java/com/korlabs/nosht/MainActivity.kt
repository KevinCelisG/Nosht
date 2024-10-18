package com.korlabs.nosht

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.korlabs.nosht.presentation.components.menu.BottomMenu
import com.korlabs.nosht.navigation.NavigationGraph
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.ui.theme.NoshtTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoshtTheme {
                Content()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Content() {
    val navHostController = rememberNavController()

    val currentBackStackEntry: NavBackStackEntry? by navHostController.currentBackStackEntryAsState()
    val currentScreen = currentBackStackEntry?.destination?.route?.split(".")?.last()

    Scaffold(
        bottomBar = {
            if (
                currentScreen == Screen.AdminProfileScreen.name ||
                currentScreen == Screen.AdminHomeScreen.name ||
                currentScreen == Screen.AdminManageEmployersScreen.name ||
                currentScreen == Screen.AdminManageMoneyScreen.name
            ) {
                BottomMenu(navHostController = navHostController, currentScreen = currentScreen)
            }
        }
    ) {
        NavigationGraph(navHostController)
    }
}