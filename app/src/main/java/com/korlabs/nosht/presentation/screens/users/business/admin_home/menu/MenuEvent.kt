package com.korlabs.nosht.presentation.screens.users.business.admin_home.menu

import com.korlabs.nosht.domain.model.Menu

sealed class MenuEvent {
    data class Add(val menu: Menu) : MenuEvent()
    data object GetRemoteMenus : MenuEvent()
    data object GetLocalMenus : MenuEvent()
}
