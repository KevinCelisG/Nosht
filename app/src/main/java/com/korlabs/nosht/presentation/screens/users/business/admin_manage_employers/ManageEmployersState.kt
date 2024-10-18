package com.korlabs.nosht.presentation.screens.users.business.admin_manage_employers

import com.korlabs.nosht.domain.model.users.Employer

data class ManageEmployersState(
    val isLoading: Boolean = false,
    val code: String? = null,
    val isTakenCode: Boolean? = null,
    val listTables: List<Employer> = emptyList()
)
