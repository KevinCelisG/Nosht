package com.korlabs.nosht.domain.remote

import androidx.lifecycle.LiveData
import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.ResourceMovement
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.model.users.Employer
import com.korlabs.nosht.domain.model.users.User
import com.korlabs.nosht.util.Resource


interface APIClient {

    val data: LiveData<Resource<List<Table>>>
    val dataContracts: LiveData<Resource<List<Contract>>>
    val dataResourcesBusiness: LiveData<Resource<List<ResourceBusiness>>>
    val dataMenus: LiveData<Resource<List<Menu>>>

    val isJoinEmployer: LiveData<Resource<String>>

    // User
    suspend fun createUser(userSignUp: UserSignUp): Resource<String>

    suspend fun getUser(uid: String?): Resource<User>

    // Tables
    suspend fun addTable(currentBusiness: Business?, table: Table): Resource<Table>

    suspend fun getTables(businessUid: String)

    // Resources business
    suspend fun createResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness
    ): Resource<ResourceBusiness>

    suspend fun deleteResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness
    ): Resource<Boolean>

    suspend fun addResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness,
        resourceMovement: ResourceMovement
    ): Resource<ResourceBusiness>

    suspend fun updateResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness
    ): Resource<ResourceBusiness>

    suspend fun getResourcesBusiness(businessUid: String)

    // Tables
    suspend fun addMenu(currentBusiness: Business?, menu: Menu): Resource<Menu>

    suspend fun getMenus(businessUid: String)

    // Employers
    suspend fun addEmployer(
        business: Business,
        employeeRoleEnum: TypeEmployeeRoleEnum,
        code: String
    ): Resource<String>

    suspend fun disabilityCode(business: Business, code: String)

    suspend fun validateCode(employer: Employer, code: String): String?

    suspend fun listenEmployerResponse(business: Business, code: String)

    suspend fun getContracts(user: User)

    suspend fun updateStatusBusiness(currentUser: User): Resource<Boolean>

    //suspend fun createGroup(group: Group): Resource<String>

    //suspend fun getGroups(business: Business): Resource<List<Group>>
}