package com.korlabs.nosht.presentation.screens.users.general.contracts

import com.korlabs.nosht.domain.model.Contract

data class ContractsState(
    val isLoading: Boolean = false,
    val contract: Contract? = null,
    val listContracts: List<Contract> = emptyList(),
    val code: String? = null,
    val isTakenCode: Boolean? = null,
    val isNewRemoteContracts: Boolean = false
)
