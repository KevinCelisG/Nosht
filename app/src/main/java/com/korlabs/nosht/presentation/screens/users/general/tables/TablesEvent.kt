package com.korlabs.nosht.presentation.screens.users.general.tables

import com.korlabs.nosht.domain.model.Table

sealed class TablesEvent {
    data class Add(val table: Table) : TablesEvent()
    data object GetRemoteTables : TablesEvent()
    data object GetLocalTables : TablesEvent()
}
