package com.korlabs.nosht.presentation.screens.users.general.tables.handleTable

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.model.MenusWithAmountInOrder
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.ResourceWithAmountInMenu
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.repository.OrdersRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: OrdersRepository
) : ViewModel() {

    var state by mutableStateOf(OrdersState())
    var listMenuItems by mutableStateOf(mutableListOf<MenusWithAmountInOrder>())
    var listResourceItems by mutableStateOf(mutableListOf<ResourceWithAmountInMenu>())

    fun onEvent(ordersEvent: OrdersEvent) {
        when (ordersEvent) {
            is OrdersEvent.Add -> {
                addOrder(ordersEvent.order)
            }

            is OrdersEvent.GetRemoteOrders -> {
                getRemoteOrders()
            }

            is OrdersEvent.GetLocalOrders -> {
                getLocalOrders()
            }

            is OrdersEvent.UpdateItemsToAddAtOrder -> {
                updateItemsToAddAtOrder(
                    ordersEvent.menusWithAmountInOrder,
                    ordersEvent.resourceWithAmountInMenu
                )
            }

            is OrdersEvent.UpdateStatus -> {
                updateStatusOrder(ordersEvent.order)
            }
        }
    }

    private fun updateStatusOrder(order: Order) {
        val currentStatus = order.status

        order.status = when (currentStatus) {
            OrderStatusEnum.PENDING -> OrderStatusEnum.COOKING
            OrderStatusEnum.COOKING -> OrderStatusEnum.READY
            OrderStatusEnum.READY -> OrderStatusEnum.SERVED
            OrderStatusEnum.SERVED -> OrderStatusEnum.PAID
            OrderStatusEnum.PAID -> TODO()
            OrderStatusEnum.ALL -> TODO()
        }

        viewModelScope.launch {
            repository.updateStatusOrder(order).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful updating the order ${result.data}")
                        state.copy(isNewRemoteOrders = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error updating the order $order")
                        state.copy(order = null)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun updateItemsToAddAtOrder(
        menusWithAmountInOrder: MutableList<MenusWithAmountInOrder>,
        resourceWithAmountInMenu: MutableList<ResourceWithAmountInMenu>
    ) {
        listMenuItems = menusWithAmountInOrder
        listResourceItems = resourceWithAmountInMenu
        Log.d(Util.TAG, "Menus set $listMenuItems")
        Log.d(Util.TAG, "Resources set $listResourceItems")
    }

    private fun addOrder(order: Order) {
        viewModelScope.launch {
            repository.addOrder(order).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful ${result.data}")
                        state.copy(order = result.data, isNewRemoteOrders = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, result.message ?: "Error adding the order $order")
                        state.copy(order = null)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun getRemoteOrders() {
        viewModelScope.launch {
            repository.getRemoteOrders()

            repository.data.observeForever { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful getting remote orders")
                        //state.copy(isNewRemoteOrders = true)
                        state.copy(listOrders = resource.data!!)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, resource.message ?: "Error getting the orders")
                        state.copy(listOrders = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun getLocalOrders() {
        /*Log.d(Util.TAG, "Start getting local tables")
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
        }*/
    }
}