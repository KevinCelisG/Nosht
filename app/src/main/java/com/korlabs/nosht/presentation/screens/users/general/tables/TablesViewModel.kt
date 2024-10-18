package com.korlabs.nosht.presentation.screens.users.general.tables

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.domain.repository.TablesRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TablesViewModel @Inject constructor(
    private val repository: TablesRepository
) : ViewModel() {

    var state by mutableStateOf(TablesState())

    fun onEvent(tablesEvent: TablesEvent) {
        when (tablesEvent) {
            is TablesEvent.Add -> {
                addTable(tablesEvent.table)
            }

            is TablesEvent.GetRemoteTables -> {
                getRemoteTables()
            }

            is TablesEvent.GetLocalTables -> {
                getLocalTables()
            }
        }
    }

    private fun addTable(table: Table) {
        viewModelScope.launch {
            repository.addTable(table).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful ${result.data}")
                        state.copy(table = result.data, isNewRemoteTables = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error adding a table $table")
                        state.copy(table = null)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun getRemoteTables() {
        viewModelScope.launch {
            repository.getRemoteTables()

            repository.data.observeForever { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful getting remote tables")
                        state.copy(isNewRemoteTables = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, resource.message ?: "Error getting the tables")
                        state.copy(listTables = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun getLocalTables() {
        Log.d(Util.TAG, "Start getting local tables")
        state = state.copy(isNewRemoteTables = false)

        viewModelScope.launch {
            repository.getLocalTables().collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful from local tables ${result.data}")
                        state.copy(listTables = result.data!!)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error getting local tables")
                        state.copy(listTables = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }
}