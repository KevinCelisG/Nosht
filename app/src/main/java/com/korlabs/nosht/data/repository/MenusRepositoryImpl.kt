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
import com.korlabs.nosht.domain.model.ResourceWithAmountInMenu
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.model.users.Employer
import com.korlabs.nosht.domain.model.users.User
import com.korlabs.nosht.domain.repository.MenusRepository
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
class MenusRepositoryImpl @Inject constructor(
    private val apiClient: APIClient,
    localDB: NoshtDatabase
) : MenusRepository {

    private val dao = localDB.dao

    private val _data = MutableLiveData<Resource<List<Menu>>>()
    override val data: LiveData<Resource<List<Menu>>> = _data

    override suspend fun addMenu(menu: Menu): Flow<Resource<Menu>> {
        return flow {
            emit(Resource.Loading(true))

            val result = apiClient.addMenu(AuthRepositoryImpl.currentUser as Business, menu)

            emit(result)
        }.catch {
            emit(Resource.Error("Error adding menu"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getRemoteMenus() {
        if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
            AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
        }

        if (AuthRepositoryImpl.currentBusinessUid != null) {
            apiClient.getMenus(AuthRepositoryImpl.currentBusinessUid!!)

            _data.value = Resource.Loading(true)

            apiClient.dataMenus.observeForever {
                if (apiClient.dataMenus.value?.data != null && AuthRepositoryImpl.currentBusinessUid != null) {
                    if (FirestoreClient.isNewMenusData) {

                        val listMenus = mutableListOf<MenuEntity>()
                        val listMenusResources = mutableListOf<MenuResourceCrossRefEntity>()

                        for (menu in apiClient.dataMenus.value?.data!!) {
                            listMenus.add(menu.toMenuEntity(AuthRepositoryImpl.currentBusinessUid!!))
                            for (item in menu.listResourceBusiness) {
                                if (menu.documentReference != null) {
                                    listMenusResources.add(
                                        MenuResourceCrossRefEntity(
                                            menuId = menu.documentReference!!,
                                            resourceId = item.resourceBusiness.documentReference!!,
                                            amount = item.amount
                                        )
                                    )
                                }
                            }
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            // The idea is leave the needed of delete the local records
                            //dao.deleteAllResources()
                            //Log.d(Util.TAG, "deleteAllResources")

                            Log.d(Util.TAG, "Insert remote menu $listMenus")
                            Log.d(Util.TAG, "Insert remote resource in menus $listMenusResources")

                            dao.insertMenus(listMenus)
                            dao.insertMenuResourceCrossRef(listMenusResources)

                            _data.postValue(Resource.Successful())

                            FirestoreClient.isNewMenusData = false
                        }
                    } else {
                        _data.value = Resource.Error("isNewMenusData = false")
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

    override suspend fun getLocalMenus(): Flow<Resource<List<Menu>>> {
        return flow {
            if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
                AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
            }

            emit(Resource.Loading(true))

            val listMenusResult = mutableListOf<Menu>()

            Log.d(
                Util.TAG,
                "Getting local menus from ${AuthRepositoryImpl.currentUser!!.email} - ${AuthRepositoryImpl.currentUser!!.uid} - ${AuthRepositoryImpl.currentUser!!.typeUserEnum.typeUser}"
            )

            for (menu in dao.getMenusByBusinessId(AuthRepositoryImpl.currentBusinessUid!!)) {
                Log.d(Util.TAG, "Get local menu ${menu.name} - ${menu.userId}")

                val listResourcesOfMenu = mutableListOf<ResourceWithAmountInMenu>()

                for (resourceOfMenu in dao.getResourceByMenuId(menu.id)) {
                    Log.d(Util.TAG, "Getting local menu resources ${resourceOfMenu.resourceId}")

                    val resourceFromLocal =
                        dao.getResourceById(resourceOfMenu.resourceId).toResourceBusiness()
                    val amountFromLocal = resourceOfMenu.amount

                    Log.d(Util.TAG, "The local resource menu is $resourceFromLocal")

                    listResourcesOfMenu.add(
                        ResourceWithAmountInMenu(
                            resourceFromLocal,
                            amountFromLocal
                        )
                    )
                }

                Log.d(Util.TAG, "Converting the menuLocal to menuModel")

                val menuFromLocal = menu.toMenu()

                menuFromLocal.listResourceBusiness = listResourcesOfMenu

                listMenusResult.add(menuFromLocal)
            }

            Log.d(
                Util.TAG,
                "Getting local menus from ${AuthRepositoryImpl.currentUser!!.email} - ${AuthRepositoryImpl.currentUser!!.uid} - ${AuthRepositoryImpl.currentUser!!.typeUserEnum}"
            )
            Log.d(Util.TAG, "Menus $listMenusResult")

            emit(Resource.Successful(data = listMenusResult.toList()))
        }.catch {
            emit(Resource.Error("Error getting resources"))
            emit(Resource.Loading(false))
        }
    }
}