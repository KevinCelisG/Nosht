package com.korlabs.nosht.presentation.screens.users.business.admin_home.menu

import com.korlabs.nosht.domain.model.Menu

data class MenuState(
    val isLoading: Boolean = false,
    val menu: Menu? = null,
    val listMenus: List<Menu> = emptyList(),
    val isNewRemoteMenus: Boolean = false
)
