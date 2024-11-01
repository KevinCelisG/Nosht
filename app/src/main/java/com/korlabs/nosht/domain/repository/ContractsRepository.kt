package com.korlabs.nosht.domain.repository

import androidx.lifecycle.LiveData
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.util.Resource
import kotlinx.coroutines.flow.Flow

interface ContractsRepository {

    val data: LiveData<Resource<List<Contract>>>
    val isJoinEmployer: LiveData<Resource<String>>

    suspend fun addEmployer(
        employeeRoleEnum: TypeEmployeeRoleEnum,
        code: String
    ): Flow<Resource<String>>

    suspend fun disabilityCode(code: String)

    suspend fun validateCode(code: String): Boolean

    suspend fun listenEmployerResponse(code: String)

    suspend fun getRemoteContracts()

    suspend fun getLocalContracts(): Flow<Resource<List<Contract>>>

    suspend fun updateStatusBusiness(): Flow<Resource<Boolean>>
}