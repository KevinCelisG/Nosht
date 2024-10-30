package com.korlabs.nosht.domain.repository

import androidx.lifecycle.LiveData
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.ResourceMovement
import com.korlabs.nosht.util.Resource
import kotlinx.coroutines.flow.Flow

interface ResourcesRepository {

    val data: LiveData<Resource<List<ResourceBusiness>>>

    suspend fun createResourceBusiness(resourceBusiness: ResourceBusiness): Flow<Resource<ResourceBusiness>>

    suspend fun deleteResourceBusiness(resourceBusiness: ResourceBusiness): Flow<Resource<Boolean>>

    suspend fun getRemoteResourceBusiness()

    suspend fun getLocalResourceBusiness(): Flow<Resource<List<ResourceBusiness>>>

    suspend fun addResourceBusiness(
        resourceBusiness: ResourceBusiness,
        resourceMovement: ResourceMovement
    ): Flow<Resource<ResourceBusiness>>

    suspend fun updateResourceBusiness(resourceBusiness: ResourceBusiness): Flow<Resource<ResourceBusiness>>
}