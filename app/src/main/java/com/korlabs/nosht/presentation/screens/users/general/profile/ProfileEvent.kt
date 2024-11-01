package com.korlabs.nosht.presentation.screens.users.general.profile

import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum

sealed class ProfileEvent {
    data object UpdateStatusBusiness : ProfileEvent()
}
