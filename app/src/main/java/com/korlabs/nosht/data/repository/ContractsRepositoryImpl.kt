package com.korlabs.nosht.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.remote.APIClient
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.data.local.NoshtDatabase
import com.korlabs.nosht.data.local.entities.ContractEntity
import com.korlabs.nosht.data.mapper.toContract
import com.korlabs.nosht.data.mapper.toContractEntity
import com.korlabs.nosht.data.remote.FirestoreClient
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.model.users.Employer
import com.korlabs.nosht.domain.repository.ContractsRepository
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
class ContractsRepositoryImpl @Inject constructor(
    private val apiClient: APIClient,
    localDB: NoshtDatabase
) : ContractsRepository {

    private val dao = localDB.dao

    private val _data = MutableLiveData<Resource<List<Contract>>>()
    override val data: LiveData<Resource<List<Contract>>> = _data

    private val _isJoinEmployer = MutableLiveData<Resource<String>>()
    override val isJoinEmployer: LiveData<Resource<String>> = _isJoinEmployer

    override suspend fun addEmployer(
        employeeRoleEnum: TypeEmployeeRoleEnum,
        code: String
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading(true))

            val result = apiClient.addEmployer(
                AuthRepositoryImpl.currentUser as Business,
                employeeRoleEnum,
                code
            )

            emit(result)
        }.catch {
            emit(Resource.Error("Error adding employer"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun disabilityCode(code: String) {
        apiClient.disabilityCode(AuthRepositoryImpl.currentUser as Business, code)
    }

    // Review
    override suspend fun validateCode(code: String): Boolean {
        AuthRepositoryImpl.currentBusinessUid =
            apiClient.validateCode(AuthRepositoryImpl.currentUser as Employer, code)
        return AuthRepositoryImpl.currentBusinessUid != null
    }

    override suspend fun listenEmployerResponse(code: String) {
        apiClient.listenEmployerResponse(AuthRepositoryImpl.currentUser as Business, code)

        apiClient.isJoinEmployer.observeForever {
            if (apiClient.isJoinEmployer.value is Resource.Successful) {
                _isJoinEmployer.value = Resource.Successful()
            }
        }
    }

    override suspend fun getRemoteContracts() {
        Log.d(Util.TAG, "Current user ${AuthRepositoryImpl.currentUser}")

        // It can be better
        if (AuthRepositoryImpl.currentUser != null) {
            apiClient.getContracts(AuthRepositoryImpl.currentUser!!)

            _data.value = Resource.Loading(true)

            apiClient.dataContracts.observeForever {
                if (apiClient.dataContracts.value?.data != null) {
                    if (FirestoreClient.isNewContractsData) {

                        val listContracts = mutableListOf<ContractEntity>()

                        for (contract in apiClient.dataContracts.value?.data!!) {
                            listContracts.add(contract.toContractEntity(AuthRepositoryImpl.currentUser!!.uid!!))
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            val localContracts =
                                dao.getContractsByUserId(AuthRepositoryImpl.currentUser!!.uid!!)
                            val remoteContractsId = listContracts.map { it.id }

                            val contractsToRemove =
                                localContracts.filter { it.id !in remoteContractsId }

                            dao.deleteContracts(contractsToRemove)
                            dao.insertContracts(listContracts)
                            _data.postValue(Resource.Successful())

                            FirestoreClient.isNewContractsData = false
                        }
                    } else {
                        _data.value = Resource.Error("isNewContractsData = false")
                    }
                } else {
                    _data.value = Resource.Error("Error getting contracts")
                }
                _data.value = Resource.Loading(false)
            }
        } else {
            _data.value = Resource.Error("There are not userUid")
        }
    }

    // Review
    override suspend fun getLocalContracts(): Flow<Resource<List<Contract>>> {
        return flow {
            emit(Resource.Loading(true))

            val listContractsResult = mutableListOf<Contract>()

            Log.d(
                Util.TAG,
                "Getting local contracts from ${AuthRepositoryImpl.currentUser!!.email} - ${AuthRepositoryImpl.currentUser!!.uid}"
            )

            for (contract in dao.getContractsByUserId(AuthRepositoryImpl.currentUser!!.uid!!)) {
                Log.d(Util.TAG, "Get local ${contract.role} ${contract.userUid}")
                listContractsResult.add(contract.toContract())

                if (contract.status == EmployerStatusEnum.AVAILABLE.status) {
                    AuthRepositoryImpl.currentBusinessUid = contract.userUid
                }
            }

            Log.d(Util.TAG, "Contracts $listContractsResult")

            emit(Resource.Successful(data = listContractsResult.toList()))
        }.catch {
            emit(Resource.Error("Error getting contracts"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun updateStatusBusiness(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))

            val response = apiClient.updateStatusBusiness(AuthRepositoryImpl.currentUser!!)

            Log.d(Util.TAG, "The new business status is ${response.data}")

            emit(response)
        }.catch {
            emit(Resource.Error("Error getting contracts"))
        }
    }
}