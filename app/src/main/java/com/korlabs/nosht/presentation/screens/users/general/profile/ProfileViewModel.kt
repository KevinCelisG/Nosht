package com.korlabs.nosht.presentation.screens.users.general.profile

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
class ProfileViewModel @Inject constructor(
    private val contractsRepository: ContractsRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileState())

    fun onEvent(profileEvent: ProfileEvent) {
        when (profileEvent) {
            is ProfileEvent.UpdateStatusBusiness -> {
                updateStatusBusiness()
            }
        }
    }

    private fun updateStatusBusiness() {
        viewModelScope.launch {
            contractsRepository.updateStatusBusiness().collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        state.copy(isOpenTheBusiness = result.data!!, isLoading = false)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }
}