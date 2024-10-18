package com.korlabs.nosht.domain.repository

import androidx.lifecycle.LiveData
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.util.Resource
import kotlinx.coroutines.flow.Flow

interface MenusRepository {

    val data: LiveData<Resource<List<Menu>>>

    suspend fun addMenu(menu: Menu): Flow<Resource<Menu>>

    suspend fun getRemoteMenus()

    suspend fun getLocalMenus(): Flow<Resource<List<Menu>>>
}