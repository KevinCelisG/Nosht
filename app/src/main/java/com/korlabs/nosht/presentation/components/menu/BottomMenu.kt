package com.korlabs.nosht.presentation.components.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.korlabs.nosht.navigation.Screen

@Composable
fun BottomMenu(
    navHostController: NavHostController,
    currentScreen: String
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.AdminHomeScreen.name,
            onClick = {
                navHostController.navigate(Screen.AdminHomeScreen)
            },
            icon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.AdminManageMoneyScreen.name,
            onClick = {
                navHostController.navigate(Screen.AdminManageMoneyScreen)
            },
            icon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Money")
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.AdminManageEmployersScreen.name,
            onClick = {
                navHostController.navigate(Screen.AdminManageEmployersScreen)
            },
            icon = {
                Icon(imageVector = Icons.Default.Face, contentDescription = "Employers")
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ProfileScreen.name,
            onClick = {
                navHostController.navigate(Screen.ProfileScreen)
            },
            icon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
            }
        )
    }
}