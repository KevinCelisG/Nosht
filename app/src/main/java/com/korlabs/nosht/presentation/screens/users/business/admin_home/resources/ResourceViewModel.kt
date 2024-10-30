package com.korlabs.nosht.presentation.screens.users.business.admin_home.resources

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.ResourceMovement
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
            is ResourceEvent.Create -> {
                createResourceBusiness(resourceEvent.resourceBusiness)
            }

            is ResourceEvent.Delete -> {
                deleteResourceBusiness(resourceEvent.resourceBusiness)
            }

            is ResourceEvent.Add -> {
                addResourceBusiness(
                    resourceEvent.resourceBusiness,
                    resourceEvent.resourceMovement
                )
            }

            is ResourceEvent.Update -> {
                updateResourceBusiness(resourceEvent.resourceBusiness)
            }

            is ResourceEvent.GetRemoteResourceBusiness -> {
                getRemoteResources()
            }

            is ResourceEvent.GetLocalResourceBusiness -> {
                getLocalResources()
            }
        }
    }

    private fun deleteResourceBusiness(
        resourceBusiness: ResourceBusiness
    ) {
        viewModelScope.launch {
            repository.deleteResourceBusiness(resourceBusiness)
                .collect { result ->
                    state = when (result) {
                        is Resource.Successful -> {
                            Log.d(Util.TAG, "Successful ${result.data}")
                            state.copy(resourceBusiness = resourceBusiness, isNewRemoteResources = true)
                        }

                        is Resource.Error -> {
                            Log.d(
                                Util.TAG,
                                result.message
                                    ?: "Error deleting the resource ${resourceBusiness.documentReference}"
                            )
                            state.copy(resourceBusiness = null)
                        }

                        is Resource.Loading -> {
                            state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun updateResourceBusiness(
        resourceBusiness: ResourceBusiness
    ) {
        viewModelScope.launch {
            repository.updateResourceBusiness(resourceBusiness)
                .collect { result ->
                    state = when (result) {
                        is Resource.Successful -> {
                            Log.d(Util.TAG, "Successful ${result.data}")
                            state.copy(resourceBusiness = result.data, isNewRemoteResources = true)
                        }

                        is Resource.Error -> {
                            Log.d(
                                Util.TAG,
                                result.message
                                    ?: "Error updating a resource ${resourceBusiness.documentReference}"
                            )
                            state.copy(resourceBusiness = null)
                        }

                        is Resource.Loading -> {
                            state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun addResourceBusiness(
        resourceBusiness: ResourceBusiness,
        resourceMovement: ResourceMovement
    ) {
        viewModelScope.launch {
            repository.addResourceBusiness(resourceBusiness, resourceMovement)
                .collect { result ->
                    state = when (result) {
                        is Resource.Successful -> {
                            Log.d(Util.TAG, "Successful ${result.data}")
                            state.copy(resourceBusiness = result.data, isNewRemoteResources = true)
                        }

                        is Resource.Error -> {
                            Log.d(
                                Util.TAG,
                                result.message
                                    ?: "Error adding a resource ${resourceBusiness.documentReference}"
                            )
                            state.copy(resourceBusiness = null)
                        }

                        is Resource.Loading -> {
                            state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun createResourceBusiness(resourceBusiness: ResourceBusiness) {
        viewModelScope.launch {
            repository.createResourceBusiness(resourceBusiness).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful ${result.data}")
                        state.copy(resourceBusiness = result.data, isNewRemoteResources = true)
                    }

                    is Resource.Error -> {
                        Log.d(
                            Util.TAG,
                            result.message ?: "Error making a resource $resourceBusiness"
                        )
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