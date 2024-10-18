package com.korlabs.nosht.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.korlabs.nosht.data.mapper.toBusiness
import com.korlabs.nosht.data.mapper.toBusinessEntity
import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.remote.APIClient
import com.korlabs.nosht.domain.remote.AuthClient
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.data.local.NoshtDatabase
import com.korlabs.nosht.data.local.entities.ContractEntity
import com.korlabs.nosht.data.local.entities.MenuEntity
import com.korlabs.nosht.data.local.entities.MenuResourceCrossRefEntity
import com.korlabs.nosht.data.local.entities.ResourceEntity
import com.korlabs.nosht.data.local.entities.TableEntity
import com.korlabs.nosht.data.mapper.toContract
import com.korlabs.nosht.data.mapper.toContractEntity
import com.korlabs.nosht.data.mapper.toEmployer
import com.korlabs.nosht.data.mapper.toEmployerEntity
import com.korlabs.nosht.data.mapper.toMenu
import com.korlabs.nosht.data.mapper.toMenuEntity
import com.korlabs.nosht.data.mapper.toResourceBusiness
import com.korlabs.nosht.data.mapper.toResourceEntity
import com.korlabs.nosht.data.mapper.toTable
import com.korlabs.nosht.data.mapper.toTableEntity
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.model.users.Employer
import com.korlabs.nosht.domain.model.users.User
import com.korlabs.nosht.domain.repository.ResourcesRepository
import com.korlabs.nosht.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesRepositoryImpl @Inject constructor(
    private val apiClient: APIClient,
    localDB: NoshtDatabase
) : ResourcesRepository {

    private val dao = localDB.dao

    private val _data = MutableLiveData<Resource<List<ResourceBusiness>>>()
    override val data: LiveData<Resource<List<ResourceBusiness>>> = _data

    override suspend fun addResourceBusiness(resourceBusiness: ResourceBusiness): Flow<Resource<ResourceBusiness>> {
        return flow {
            emit(Resource.Loading(true))

            val result = apiClient.addResourceBusiness(
                AuthRepositoryImpl.currentUser as Business,
                resourceBusiness
            )

            emit(result)
        }.catch {
            emit(Resource.Error("Error adding resource business"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getRemoteResourceBusiness() {
        if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
            AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
        }

        if (AuthRepositoryImpl.currentBusinessUid != null) {
            apiClient.getResourcesBusiness(AuthRepositoryImpl.currentBusinessUid!!)

            _data.value = Resource.Loading(true)

            apiClient.dataResourcesBusiness.observeForever {
                if (apiClient.dataResourcesBusiness.value?.data != null && AuthRepositoryImpl.currentBusinessUid != null) {
                    if (FirestoreClient.isNewResourcesData) {
                        Log.d(
                            Util.TAG,
                            "There are new resources data in apiClient ${apiClient.dataResourcesBusiness.value?.data!!}"
                        )

                        val listResources = mutableListOf<ResourceEntity>()

                        for (resource in apiClient.dataResourcesBusiness.value?.data!!) {
                            listResources.add(resource.toResourceEntity(AuthRepositoryImpl.currentBusinessUid!!))
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            //dao.deleteAllResources()
                            //Log.d(Util.TAG, "deleteAllResources")

                            listResources.forEach { _ ->
                                Log.d(
                                    Util.TAG,
                                    "Insert local resource in $AuthRepositoryImpl.currentBusinessUid"
                                )
                            }
                            dao.insertResources(listResources)
                            _data.postValue(Resource.Successful())
                        }
                    } else {
                        _data.value = Resource.Error("isNewResourcesData = false")
                    }
                } else {
                    _data.value = Resource.Error("Error getting tables")
                }
                _data.value = Resource.Loading(false)
            }
        } else {
            _data.value = Resource.Error("There are not businessUid")
        }
    }

    override suspend fun getLocalResourceBusiness(): Flow<Resource<List<ResourceBusiness>>> {
        return flow {
            if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
                AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
            }

            emit(Resource.Loading(true))

            val listResourcesResult = mutableListOf<ResourceBusiness>()

            Log.d(
                Util.TAG,
                "Getting local resources from ${AuthRepositoryImpl.currentUser!!.email} - ${AuthRepositoryImpl.currentUser!!.uid}"
            )

            for (resourceBusiness in dao.getResourcesByBusinessId(AuthRepositoryImpl.currentBusinessUid!!)) {
                Log.d(Util.TAG, "Get local ${resourceBusiness.name} - ${resourceBusiness.userId}")
                listResourcesResult.add(resourceBusiness.toResourceBusiness())
            }

            Log.d(
                Util.TAG,
                "Getting local resources from ${AuthRepositoryImpl.currentUser!!.email} - ${AuthRepositoryImpl.currentUser!!.uid}"
            )
            Log.d(Util.TAG, "resources $listResourcesResult")

            emit(Resource.Successful(data = listResourcesResult.toList()))
        }.catch {
            emit(Resource.Error("Error getting resources"))
            emit(Resource.Loading(false))
        }
    }
}