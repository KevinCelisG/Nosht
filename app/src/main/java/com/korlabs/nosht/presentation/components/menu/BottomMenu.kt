package com.korlabs.nosht.presentation.components.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.ui.theme.Dimens
import com.korlabs.nosht.util.Util

@Composable
fun BottomMenu(
    navHostController: NavHostController,
    currentScreen: String
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(Util.heightPercent(percent = Dimens.bottomMenuHeight))
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.AdminHomeScreen.name,
            onClick = {
                navHostController.navigate(Screen.AdminHomeScreen)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = colorResource(id = R.color.item_menu)
                )
            }
        )

        NavigationBarItem(
            selected = currentScreen.startsWith(Screen.OrdersScreen().name),
            onClick = {
                navHostController.navigate(Screen.OrdersScreen())
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Orders",
                    tint = colorResource(id = R.color.item_menu)
                )
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.AdminManageEmployersScreen.name,
            onClick = {
                navHostController.navigate(Screen.AdminManageEmployersScreen)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Employers",
                    tint = colorResource(id = R.color.item_menu)
                )
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ReportsScreen.name,
            onClick = {
                navHostController.navigate(Screen.ReportsScreen)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Reports",
                    tint = colorResource(id = R.color.item_menu)
                )
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ProfileScreen.name,
            onClick = {
                navHostController.navigate(Screen.ProfileScreen)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = colorResource(id = R.color.item_menu)
                )
            }
        )
    }
}