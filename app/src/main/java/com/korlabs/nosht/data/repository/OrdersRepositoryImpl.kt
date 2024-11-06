package com.korlabs.nosht.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.korlabs.nosht.domain.remote.APIClient
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.data.local.NoshtDatabase
import com.korlabs.nosht.data.local.entities.TableEntity
import com.korlabs.nosht.data.mapper.toTableEntity
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.repository.OrdersRepository
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
class OrdersRepositoryImpl @Inject constructor(
    private val apiClient: APIClient,
    localDB: NoshtDatabase
) : OrdersRepository {

    private val dao = localDB.dao

    private val _data = MutableLiveData<Resource<List<Order>>>()
    override val data: LiveData<Resource<List<Order>>> = _data

    override suspend fun addOrder(order: Order): Flow<Resource<Order>> {
        return flow {
            emit(Resource.Loading(true))

            val result = apiClient.addOrder(AuthRepositoryImpl.currentBusinessUid!!, order)

            emit(result)
        }.catch {
            emit(Resource.Error("Error adding order"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getRemoteOrders() {
        // It can be better
        if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
            AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
        }

        if (AuthRepositoryImpl.currentBusinessUid != null) {
            apiClient.getOrders(AuthRepositoryImpl.currentBusinessUid!!)

            _data.value = Resource.Loading(true)

            apiClient.dataOrders.observeForever {
                if (apiClient.dataOrders.value?.data != null && AuthRepositoryImpl.currentBusinessUid != null) {
                    if (FirestoreClient.isNewOrdersData) {
                        Log.d(
                            Util.TAG,
                            "There are new order data in apiClient ${apiClient.dataOrders.value?.data!!}"
                        )

                        /*val listOrders = mutableListOf<TableEntity>()

                        for (order in apiClient.data.value?.data!!) {
                            listOrders.add(order.toTableEntity(AuthRepositoryImpl.currentBusinessUid!!))
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            val localTables =
                                dao.getTablesByBusinessId(AuthRepositoryImpl.currentBusinessUid!!)
                            val remoteTablesId = listOrders.map { it.id }

                            val tablesToRemove = localTables.filter { it.id !in remoteTablesId }

                            dao.deleteTables(tablesToRemove)
                            dao.insertTables(listOrders)
                            _data.postValue(Resource.Successful())

                            FirestoreClient.isNewTablesData = false
                        }*/

                        _data.postValue(Resource.Successful(data = apiClient.dataOrders.value!!.data))
                        FirestoreClient.isNewOrdersData = false
                    } else {
                        _data.value = Resource.Error("isNewTablesOrders = false")
                    }
                } else {
                    _data.value = Resource.Error("Error getting orders")
                }

                _data.value = Resource.Loading(false)
            }
        } else {
            _data.value = Resource.Error("There are not businessUid")
        }
    }

    override suspend fun updateStatusOrder(order: Order): Flow<Resource<Boolean>> {
        if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
            AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
        }

        return flow {
            emit(Resource.Loading(true))

            val result = apiClient.updateStatusOrder(AuthRepositoryImpl.currentBusinessUid!!, order)

            emit(result)
        }.catch {
            emit(Resource.Error("Error updating status order"))
            emit(Resource.Loading(false))
        }
    }

    /*override suspend fun getLocalOrders(): Flow<Resource<List<Order>>> {
        return flow {
            if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
                AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
            }

            emit(Resource.Loading(true))

            val listTablesResult = mutableListOf<Table>()

            Log.d(
                Util.TAG,
                "Getting local tables from ${AuthRepositoryImpl.currentUser!!.email} - ${AuthRepositoryImpl.currentUser!!.uid}"
            )

            Log.d(
                Util.TAG,
                "The owner of this tables AuthRepositoryImpl.currentBusinessUid ${AuthRepositoryImpl.currentBusinessUid!!}"
            )

            for (table in dao.getTablesByBusinessId(AuthRepositoryImpl.currentBusinessUid!!)) {
                Log.d(Util.TAG, "Get local table $table")
                listTablesResult.add(table.toTable())
            }

            Log.d(Util.TAG, "Getting successfully the local tables $listTablesResult")

            emit(Resource.Successful(data = listTablesResult.toList()))
        }.catch {
            emit(Resource.Error("Error in the sign up catch"))
            emit(Resource.Loading(false))
        }
    }*/
}