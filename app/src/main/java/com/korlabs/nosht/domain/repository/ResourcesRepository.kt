package com.korlabs.nosht.domain.repository

import androidx.lifecycle.LiveData
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.util.Resource
import kotlinx.coroutines.flow.Flow

interface ResourcesRepository {

    val data: LiveData<Resource<List<ResourceBusiness>>>

    suspend fun addResourceBusiness(resourceBusiness: ResourceBusiness): Flow<Resource<ResourceBusiness>>

    suspend fun getRemoteResourceBusiness()

    suspend fun getLocalResourceBusiness(): Flow<Resource<List<ResourceBusiness>>>
}