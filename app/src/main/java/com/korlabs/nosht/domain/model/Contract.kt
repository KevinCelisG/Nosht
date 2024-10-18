package com.korlabs.nosht.domain.model

import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum

data class Contract(
    var userUid: String,
    var role: TypeEmployeeRoleEnum,
    var status: EmployerStatusEnum,
    var documentReference: String? = null
)
