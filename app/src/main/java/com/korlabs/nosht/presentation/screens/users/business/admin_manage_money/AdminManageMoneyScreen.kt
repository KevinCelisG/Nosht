package com.korlabs.nosht.presentation.screens.users.business.admin_manage_money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.korlabs.nosht.presentation.components.text.TextTitleCustom

@Composable
fun AdminManageMoneyScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextTitleCustom(title = "Money")
    }
}