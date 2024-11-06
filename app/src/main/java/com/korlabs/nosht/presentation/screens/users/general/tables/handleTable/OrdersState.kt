package com.korlabs.nosht.presentation.screens.users.general.tables.handleTable

import com.korlabs.nosht.domain.model.Order

data class OrdersState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val listOrders: List<Order> = emptyList(),
    val isNewRemoteOrders: Boolean = false
)
