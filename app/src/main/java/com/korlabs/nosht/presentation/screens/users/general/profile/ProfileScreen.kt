package com.korlabs.nosht.presentation.screens.users.general.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.korlabs.nosht.NoshtApplication
import com.korlabs.nosht.R
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.spacers.SpacerVertical
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.ui.theme.Dimens
import com.korlabs.nosht.util.Util


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel
) {
    val currentUser = AuthRepositoryImpl.currentUser!!
    val isABusiness = currentUser.typeUserEnum == TypeUserEnum.BUSINESS
    var isOpen = false

    if (isABusiness) {
        isOpen = (currentUser as Business).isOpenTheBusiness!!
        Log.d(Util.TAG, "Is a business and is $isOpen")
    }

    var openBusiness by rememberSaveable { mutableStateOf(isOpen) }

    ColumnCustom {
        TextTitleCustom(title = stringResource(id = R.string.profile_title))

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile photo", modifier = Modifier
                .size(Util.widthPercent(percent = 0.25f))
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )

        Spacer(modifier = Modifier.height(Util.heightPercent(percent = 0.02f)))

        Text(
            text = "${currentUser.name} ${currentUser.lastName}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )

        if (isABusiness) {
            Text(
                text = (currentUser as Business).businessName!!,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Text(
                stringResource(id = R.string.employer),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(Util.heightPercent(percent = 0.1f)))

        if (isABusiness) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Util.heightPercent(percent = 0.05f))
                    .clickable {
                        profileViewModel.onEvent(ProfileEvent.UpdateStatusBusiness)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Open",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(Util.widthPercent(percent = 0.02f)))

                Log.d(Util.TAG, "Put the value in $openBusiness")

                Text(
                    text = if (openBusiness) stringResource(id = R.string.close_business) else stringResource(
                        id = R.string.open_business
                    ),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Util.heightPercent(percent = 0.05f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Preferences",
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(Util.widthPercent(percent = 0.02f)))

            Text(
                text = stringResource(id = R.string.preferences),
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Util.heightPercent(percent = 0.05f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Help",
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(Util.widthPercent(percent = 0.02f)))

            Text(
                text = stringResource(id = R.string.help),
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.height(Util.heightPercent(percent = 0.02f)))

        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(Util.heightPercent(percent = 0.02f)))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Util.heightPercent(percent = 0.05f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Go out",
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(Util.widthPercent(percent = 0.02f)))

            Text(
                text = stringResource(id = R.string.signOut),
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.height(Util.heightPercent(percent = 0.08f)))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Util.heightPercent(percent = 0.1f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(id = R.string.version_app_message, "1.2.3"),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }

    LaunchedEffect(key1 = profileViewModel.state.isLoading) {
        Log.d(
            Util.TAG,
            "Arrived the next new status for the business ${profileViewModel.state.isOpenTheBusiness}"
        )
        openBusiness = profileViewModel.state.isOpenTheBusiness
    }

    LaunchedEffect(isOpen) {
        openBusiness = isOpen
    }
}