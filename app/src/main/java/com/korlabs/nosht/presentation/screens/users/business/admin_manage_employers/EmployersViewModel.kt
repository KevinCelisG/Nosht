package com.korlabs.nosht.presentation.screens.users.business.admin_manage_employers

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.repository.ContractsRepository
import com.korlabs.nosht.domain.repository.TablesRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployersViewModel @Inject constructor(
    private val contractsRepository: ContractsRepository,
    private val tablesRepository: TablesRepository
) : ViewModel() {

    var isNewRemoteEmployers: Boolean = false

    var state by mutableStateOf(ManageEmployersState())
    private var currentCode: String = ""

    fun onEvent(manageEmployersEvent: ManageEmployersEvent) {
        when (manageEmployersEvent) {
            is ManageEmployersEvent.Add -> {
                addEmployer(manageEmployersEvent.employeeRoleEnum)
            }

            is ManageEmployersEvent.GetRemoteEmployers -> {
                getRemoteEmployers()
            }

            is ManageEmployersEvent.GetLocalEmployers -> {
                getLocalEmployers()
            }

            is ManageEmployersEvent.DisabilityCode -> {
                disabilityCode()
            }

            is ManageEmployersEvent.ListenEmployerResponse -> {
                listenEmployerResponse()
            }

            is ManageEmployersEvent.ValidateCode -> {
                validateCode(manageEmployersEvent.code)
            }
        }
    }

    private fun listenEmployerResponse() {
        viewModelScope.launch {
            contractsRepository.listenEmployerResponse(currentCode)

            contractsRepository.isJoinEmployer.observeForever { resource ->
                if (resource is Resource.Successful) {
                    state = state.copy(isTakenCode = true)
                }
            }
        }
    }

    // Check if is good idea return something, is probably that yes
    private fun disabilityCode() {
        viewModelScope.launch {
            contractsRepository.disabilityCode(currentCode)
        }
    }

    private fun addEmployer(employeeRoleEnum: TypeEmployeeRoleEnum) {
        viewModelScope.launch {
            currentCode = Util.generateUniqueCode()

            contractsRepository.addEmployer(employeeRoleEnum, currentCode).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful ${result.data}")
                        state.copy(code = currentCode)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error adding a table $employeeRoleEnum")
                        state.copy(code = null)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    // TO DO
    private fun getRemoteEmployers() {
        viewModelScope.launch {
            tablesRepository.getRemoteTables()

            tablesRepository.data.observeForever { resource ->
                when (resource) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful getting remote tables in employers")
                        isNewRemoteEmployers = true
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, resource.message ?: "Error getting the tables")
                        state = state.copy(listTables = emptyList())
                    }

                    is Resource.Loading -> {
                        state = state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun validateCode(code: String) {
        viewModelScope.launch {
            val isValid = contractsRepository.validateCode(code)
            state = state.copy(isTakenCode = isValid)
        }
    }

    // TO DO
    private fun getLocalEmployers() {
        viewModelScope.launch {
            isNewRemoteEmployers = false

            /*repository.getLocalTables().collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful from local ${result.data}")
                        state.copy(listTables = result.data!!)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error getting local table")
                        state.copy(listTables = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }*/
        }
    }
}