package com.korlabs.nosht.presentation.screens.users.general.contracts

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.domain.repository.ContractsRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractsViewModel @Inject constructor(
    private val repository: ContractsRepository
) : ViewModel() {

    var state by mutableStateOf(ContractsState())
    private var currentCode: String = ""

    fun onEvent(contractEvent: ContractsEvent) {
        when (contractEvent) {
            is ContractsEvent.Add -> {
                addContract(contractEvent.employeeRoleEnum)
            }

            is ContractsEvent.GetRemoteContracts -> {
                getRemoteContracts()
            }

            is ContractsEvent.GetLocalContracts -> {
                getLocalContracts()
            }

            is ContractsEvent.DisabilityCode -> {
                disabilityCode()
            }

            is ContractsEvent.ListenEmployerResponse -> {
                listenEmployerResponse()
            }

            is ContractsEvent.ValidateCode -> {
                validateCode(contractEvent.code)
            }
        }
    }

    private fun listenEmployerResponse() {
        viewModelScope.launch {
            repository.listenEmployerResponse(currentCode)

            repository.isJoinEmployer.observeForever { resource ->
                if (resource is Resource.Successful) {
                    state = state.copy(isTakenCode = true)
                }
            }
        }
    }

    private fun disabilityCode() {
        viewModelScope.launch {
            repository.disabilityCode(currentCode)
        }
    }

    private fun addContract(employeeRoleEnum: TypeEmployeeRoleEnum) {
        viewModelScope.launch {
            currentCode = Util.generateUniqueCode()

            repository.addEmployer(employeeRoleEnum, currentCode).collect { result ->
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

    private fun validateCode(code: String) {
        viewModelScope.launch {
            val isValid = repository.validateCode(code)
            state = state.copy(isTakenCode = isValid)
        }
    }

    private fun getRemoteContracts() {
        viewModelScope.launch {
            repository.getRemoteContracts()

            repository.data.observeForever { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful getting remote contracts")
                        state.copy(isNewRemoteContracts = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, resource.message ?: "Error getting the contracts")
                        state.copy(listContracts = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun getLocalContracts() {
        viewModelScope.launch {
            state = state.copy(isNewRemoteContracts = false)

            repository.getLocalContracts().collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful from local contracts ${result.data}")
                        state.copy(listContracts = result.data!!)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error getting local contracs")
                        state.copy(listContracts = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }
}