package com.korlabs.nosht.presentation.screens.users.general.contracts

import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.presentation.screens.users.business.admin_manage_employers.ManageEmployersEvent

sealed class ContractsEvent {
    data class Add(val employeeRoleEnum: TypeEmployeeRoleEnum) : ContractsEvent()
    data object GetRemoteContracts : ContractsEvent()
    data object GetLocalContracts : ContractsEvent()
    data object ListenEmployerResponse : ContractsEvent()
    data object DisabilityCode : ContractsEvent()
    data class ValidateCode(val code: String) : ContractsEvent()
}
