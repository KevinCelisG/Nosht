package com.korlabs.nosht.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.enums.MenuStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeMeasurementEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum

@Entity(tableName = "EmployerEntity")
data class EmployerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phone: String
)

@Entity(tableName = "ContractEntity")
data class ContractEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val role: String,
    val status: String,
    var userUid: String
)

@Entity(tableName = "BusinessEntity")
data class BusinessEntity(
    @PrimaryKey val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val location: String,
    val businessName: String,
    val isOpenTheBusiness: Boolean
)

@Entity(tableName = "TableEntity")
data class TableEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val status: String
)

@Entity(tableName = "ResourceEntity")
data class ResourceEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    var minStock: Short,
    var maxStock: Short,
    var price: Float,
    var amount: Float,
    var typeMeasurement: String,
    val type: String,
)

@Entity(tableName = "MenuEntity")
data class MenuEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val status: String,
    val price: Float,
    val isDynamic: Boolean
)

@Entity(tableName = "MenuResourceCrossRefEntity", primaryKeys = ["menuId", "resourceId"])
data class MenuResourceCrossRefEntity(
    val menuId: String,
    val resourceId: String,
    val amount: Float
)