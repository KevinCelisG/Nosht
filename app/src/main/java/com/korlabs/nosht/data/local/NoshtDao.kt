package com.korlabs.nosht.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.korlabs.nosht.data.local.entities.BusinessEntity
import com.korlabs.nosht.data.local.entities.ContractEntity
import com.korlabs.nosht.data.local.entities.EmployerEntity
import com.korlabs.nosht.data.local.entities.MenuEntity
import com.korlabs.nosht.data.local.entities.MenuResourceCrossRefEntity
import com.korlabs.nosht.data.local.entities.ResourceEntity
import com.korlabs.nosht.data.local.entities.TableEntity
import com.korlabs.nosht.domain.model.users.Employer

@Dao
interface NoshtDao {

    @Query("DELETE FROM EmployerEntity")
    suspend fun deleteAllEmployers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployer(employer: EmployerEntity)

    @Query("DELETE FROM BusinessEntity")
    suspend fun deleteAllBusiness()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: BusinessEntity)

    @Transaction
    suspend fun loginBusiness(currentBusiness: BusinessEntity) {
        deleteAllBusiness()
        deleteAllEmployers()
        insertBusiness(currentBusiness)
    }

    @Transaction
    suspend fun loginEmployer(currentEmployer: EmployerEntity) {
        deleteAllBusiness()
        deleteAllEmployers()
        insertEmployer(currentEmployer)
    }

    @Query("SELECT * FROM EmployerEntity")
    suspend fun getEmployer(): EmployerEntity?

    @Query("SELECT * FROM BusinessEntity")
    suspend fun getBusiness(): BusinessEntity?

    // Tables
    @Query("DELETE FROM TableEntity")
    suspend fun deleteAllTables()

    @Query("SELECT * FROM TableEntity WHERE userId = :businessId")
    suspend fun getTablesByBusinessId(businessId: String): List<TableEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTables(tables: List<TableEntity>)

    /*@Query("SELECT * FROM TableEntity")
    suspend fun getAllTables(): List<TableEntity>*/

    // Contracts
    @Query("DELETE FROM ContractEntity")
    suspend fun deleteAllContracts()

    @Query("SELECT * FROM ContractEntity WHERE userId = :userId")
    suspend fun getContractsByUserId(userId: String): List<ContractEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContracts(contracts: List<ContractEntity>)

    // Resource
    @Query("DELETE FROM ResourceEntity")
    suspend fun deleteAllResources()

    @Query("SELECT * FROM ResourceEntity WHERE userId = :businessId")
    suspend fun getResourcesByBusinessId(businessId: String): List<ResourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResources(resources: List<ResourceEntity>)

    // Menu
    @Query("SELECT * FROM MenuEntity WHERE userId = :businessId")
    suspend fun getMenusByBusinessId(businessId: String): List<MenuEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenus(menus: List<MenuEntity>)

    @Query("SELECT * FROM MenuResourceCrossRefEntity WHERE menuId = :menuId")
    suspend fun getResourceByMenuId(menuId: String): List<MenuResourceCrossRefEntity>

    @Query("SELECT * FROM ResourceEntity WHERE id = :resourceId")
    suspend fun getResourceById(resourceId: String): ResourceEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuResourceCrossRef(menuResourceCrossRefEntity: List<MenuResourceCrossRefEntity>)


    /*@Query(
        """
            SELECT * 
            FROM companylistingentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == symbol
        """
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>*/
}