package com.korlabs.nosht.presentation.components.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.spacers.SpacerVertical
import com.korlabs.nosht.ui.theme.Dimens
import com.korlabs.nosht.util.Util

@Composable
fun HeaderComponent(
    navHostController: NavHostController,
    titleScreen: String = ""
) {
    val currentScreen =
        navHostController.currentBackStackEntry?.destination?.route?.split(".")?.last()
    val title = titleScreen.ifBlank {
        AuthRepositoryImpl.currentUser!!.name!!
    }
    val mainlyTitle =
        if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) (AuthRepositoryImpl.currentUser as Business).businessName!! else AuthRepositoryImpl.currentUser!!.name!!

    SpacerVertical(0.02f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(Util.heightPercent(percent = Dimens.headerHeight)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Util.heightPercent(percent = Dimens.headerHeight * 0.4f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(Util.heightPercent(percent = Dimens.headerHeight * 0.3f))
                    .clickable { navHostController.navigate(Screen.ProfileScreen) }
            )

            Text(
                text = mainlyTitle,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(Util.heightPercent(percent = Dimens.headerHeight * 0.3f))
                    .clickable { /* TO DO - Implement notification screen */ }
            )
        }

        Text(
            text =
            if (titleScreen.isBlank()) stringResource(
                id = R.string.message_hello,
                title
            ) else title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

        SpacerVertical(Dimens.headerHeight * 0.15f)
    }
}