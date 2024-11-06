package com.korlabs.nosht.di

import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.data.repository.ContractsRepositoryImpl
import com.korlabs.nosht.data.repository.MenusRepositoryImpl
import com.korlabs.nosht.data.repository.OrdersRepositoryImpl
import com.korlabs.nosht.data.repository.ResourcesRepositoryImpl
import com.korlabs.nosht.data.repository.TablesRepositoryImpl
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.domain.repository.ContractsRepository
import com.korlabs.nosht.domain.repository.MenusRepository
import com.korlabs.nosht.domain.repository.OrdersRepository
import com.korlabs.nosht.domain.repository.ResourcesRepository
import com.korlabs.nosht.domain.repository.TablesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindContractsRepository(
        contractsRepositoryImpl: ContractsRepositoryImpl
    ): ContractsRepository

    @Binds
    @Singleton
    abstract fun bindMenusRepository(
        menusRepositoryImpl: MenusRepositoryImpl
    ): MenusRepository

    @Binds
    @Singleton
    abstract fun bindResourcesRepository(
        resourcesRepositoryImpl: ResourcesRepositoryImpl
    ): ResourcesRepository

    @Binds
    @Singleton
    abstract fun bindTablesRepository(
        tablesRepositoryImpl: TablesRepositoryImpl
    ): TablesRepository

    @Binds
    @Singleton
    abstract fun ordersTablesRepository(
        ordersRepositoryImpl: OrdersRepositoryImpl
    ): OrdersRepository
}