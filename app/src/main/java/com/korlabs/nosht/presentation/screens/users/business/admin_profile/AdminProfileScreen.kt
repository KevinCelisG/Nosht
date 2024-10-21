package com.korlabs.nosht.presentation.screens.users.business.admin_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.text.TextTitleCustom


@Composable
fun AdminProfileScreen() {
    ColumnCustom {
        TextTitleCustom(title = "Profile")
    }
}