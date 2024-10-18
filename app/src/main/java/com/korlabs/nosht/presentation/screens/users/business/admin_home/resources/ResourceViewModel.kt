package com.korlabs.nosht.presentation.screens.users.business.admin_home.resources

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.domain.repository.ResourcesRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(
    private val repository: ResourcesRepository
) : ViewModel() {

    var state by mutableStateOf(ResourceState())

    fun onEvent(resourceEvent: ResourceEvent) {
        when (resourceEvent) {
            is ResourceEvent.Add -> {
                addResourceBusiness(resourceEvent.resourceBusiness)
            }

            is ResourceEvent.GetRemoteResourceBusiness -> {
                getRemoteResources()
            }

            is ResourceEvent.GetLocalResourceBusiness -> {
                getLocalResources()
            }
        }
    }

    private fun addResourceBusiness(resourceBusiness: ResourceBusiness) {
        viewModelScope.launch {
            repository.addResourceBusiness(resourceBusiness).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful ${result.data}")
                        state.copy(resourceBusiness = result.data, isNewRemoteResources = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error adding a table $resourceBusiness")
                        state.copy(resourceBusiness = null)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun getRemoteResources() {
        viewModelScope.launch {
            repository.getRemoteResourceBusiness()

            repository.data.observeForever { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful getting remote resources")
                        state.copy(isNewRemoteResources = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error getting the resources")
                        state.copy(listResourceBusiness = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun getLocalResources() {
        viewModelScope.launch {
            state = state.copy(isNewRemoteResources = false)

            repository.getLocalResourceBusiness().collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful from local ${result.data}")
                        state.copy(listResourceBusiness = result.data!!)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error getting local resources")
                        state.copy(listResourceBusiness = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }
}