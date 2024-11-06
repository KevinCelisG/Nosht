package com.korlabs.nosht.domain.repository

import androidx.lifecycle.LiveData
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.util.Resource
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

    val data: LiveData<Resource<List<Order>>>

    suspend fun addOrder(order: Order): Flow<Resource<Order>>

    suspend fun getRemoteOrders()

    suspend fun updateStatusOrder(order: Order): Flow<Resource<Boolean>>

    //suspend fun getLocalOrders(): Flow<Resource<List<Order>>>
}