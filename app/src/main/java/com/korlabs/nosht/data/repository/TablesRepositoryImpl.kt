package com.korlabs.nosht.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.remote.APIClient
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.data.local.NoshtDatabase
import com.korlabs.nosht.data.local.entities.TableEntity
import com.korlabs.nosht.data.mapper.toTable
import com.korlabs.nosht.data.mapper.toTableEntity
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.repository.TablesRepository
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
class TablesRepositoryImpl @Inject constructor(
    private val apiClient: APIClient,
    localDB: NoshtDatabase
) : TablesRepository {

    private val dao = localDB.dao

    private val _data = MutableLiveData<Resource<List<Table>>>()
    override val data: LiveData<Resource<List<Table>>> = _data

    override suspend fun addTable(table: Table): Flow<Resource<Table>> {
        return flow {
            emit(Resource.Loading(true))

            val result = apiClient.addTable(AuthRepositoryImpl.currentUser as Business, table)

            emit(result)
        }.catch {
            emit(Resource.Error("Error adding table"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getRemoteTables() {
        // It can be better
        if (AuthRepositoryImpl.currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
            AuthRepositoryImpl.currentBusinessUid = AuthRepositoryImpl.currentUser!!.uid
        }

        if (AuthRepositoryImpl.currentBusinessUid != null) {
            Log.d(
                Util.TAG,
                "In getRemoteTables of repository there are a currentBusinessId that is not null"
            )

            apiClient.getTables(AuthRepositoryImpl.currentBusinessUid!!)

            _data.value = Resource.Loading(true)

            apiClient.data.observeForever {
                if (apiClient.data.value?.data != null && AuthRepositoryImpl.currentBusinessUid != null) {
                    if (FirestoreClient.isNewTablesData) {
                        Log.d(
                            Util.TAG,
                            "There are new tables data in apiClient ${apiClient.data.value?.data!!}"
                        )

                        val listTables = mutableListOf<TableEntity>()

                        for (table in apiClient.data.value?.data!!) {
                            listTables.add(table.toTableEntity(AuthRepositoryImpl.currentBusinessUid!!))
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            //dao.deleteAllTables()
                            //Log.d(Util.TAG, "DeleteAllTables")

                            listTables.forEach { _ ->
                                Log.d(
                                    Util.TAG,
                                    "Insert local table in ${AuthRepositoryImpl.currentBusinessUid}"
                                )
                            }
                            dao.insertTables(listTables)
                            _data.postValue(Resource.Successful())

                            FirestoreClient.isNewTablesData = false
                        }
                    } else {
                        _data.value = Resource.Error("isNewTablesData = false")
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

    override suspend fun getLocalTables(): Flow<Resource<List<Table>>> {
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
    }
}