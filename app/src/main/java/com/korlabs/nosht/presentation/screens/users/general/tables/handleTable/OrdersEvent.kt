package com.korlabs.nosht.presentation.screens.users.general.tables.handleTable

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.korlabs.nosht.domain.model.MenusWithAmountInOrder
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.ResourceWithAmountInMenu
import com.korlabs.nosht.domain.model.Table

sealed class OrdersEvent {
    data class Add(val order: Order) : OrdersEvent()
    data object GetRemoteOrders : OrdersEvent()
    data object GetLocalOrders : OrdersEvent()
    data class UpdateStatus(val order: Order) : OrdersEvent()
    data class UpdateItemsToAddAtOrder(
        val menusWithAmountInOrder: MutableList<MenusWithAmountInOrder>,
        val resourceWithAmountInMenu: MutableList<ResourceWithAmountInMenu>
    ) : OrdersEvent()
}
