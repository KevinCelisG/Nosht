package com.korlabs.nosht.presentation.screens.users.business.admin_manage_employers

import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum

sealed class ManageEmployersEvent {
    data class Add(val employeeRoleEnum: TypeEmployeeRoleEnum) : ManageEmployersEvent()
    data object GetRemoteEmployers : ManageEmployersEvent()
    data object GetLocalEmployers : ManageEmployersEvent()
    data object ListenEmployerResponse : ManageEmployersEvent()
    data object DisabilityCode : ManageEmployersEvent()
    data class ValidateCode(val code: String) : ManageEmployersEvent()
}
