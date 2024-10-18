package com.korlabs.nosht.domain.repository

import androidx.lifecycle.LiveData
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.util.Resource
import kotlinx.coroutines.flow.Flow

interface TablesRepository {

    val data: LiveData<Resource<List<Table>>>

    suspend fun addTable(table: Table): Flow<Resource<Table>>

    suspend fun getRemoteTables()

    suspend fun getLocalTables(): Flow<Resource<List<Table>>>
}