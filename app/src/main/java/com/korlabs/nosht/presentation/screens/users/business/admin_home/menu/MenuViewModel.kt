package com.korlabs.nosht.presentation.screens.users.business.admin_home.menu

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.domain.repository.MenusRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: MenusRepository
) : ViewModel() {

    var state by mutableStateOf(MenuState())

    fun onEvent(menuEvent: MenuEvent) {
        when (menuEvent) {
            is MenuEvent.Add -> {
                addMenu(menuEvent.menu)
            }

            is MenuEvent.GetRemoteMenus -> {
                getRemoteMenus()
            }

            is MenuEvent.GetLocalMenus -> {
                getLocalMenus()
            }
        }
    }

    private fun addMenu(menu: Menu) {
        viewModelScope.launch {
            repository.addMenu(menu).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful ${result.data}")
                        state.copy(menu = result.data)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error adding a table $menu")
                        state.copy(menu = null)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun getRemoteMenus() {
        viewModelScope.launch {
            Log.d(Util.TAG, "------------- Starting to getting remote menus -------------")

            repository.getRemoteMenus()

            repository.data.observeForever { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful getting remote menus")
                        state.copy(isNewRemoteMenus = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, resource.message ?: "Error getting the menus")
                        state.copy(listMenus = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun getLocalMenus() {
        viewModelScope.launch {
            state = state.copy(isNewRemoteMenus = false)

            repository.getLocalMenus().collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful from local menus ${result.data}")
                        state.copy(listMenus = result.data!!)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error getting local contracs")
                        state.copy(listMenus = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }
}