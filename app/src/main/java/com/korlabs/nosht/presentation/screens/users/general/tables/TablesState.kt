package com.korlabs.nosht.presentation.screens.users.general.tables

import com.korlabs.nosht.domain.model.Table

data class TablesState(
    val isLoading: Boolean = false,
    val table: Table? = null,
    val listTables: List<Table> = emptyList(),
    val isNewRemoteTables: Boolean = false
)
