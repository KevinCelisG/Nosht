package com.korlabs.nosht.util

import com.korlabs.nosht.domain.model.enums.MenuStatusEnum
import com.korlabs.nosht.domain.model.enums.TableStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import com.korlabs.nosht.domain.model.enums.employee.CodeStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.model.users.Employer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.random.Random

class Util {
    companion object {

        const val TAG = "AndroidRuntime"

        fun getTableStatus(tableStatus: String): TableStatusEnum {
            return when (tableStatus) {
                TableStatusEnum.AVAILABLE.status -> TableStatusEnum.AVAILABLE
                TableStatusEnum.NOT_AVAILABLE.status -> TableStatusEnum.NOT_AVAILABLE
                TableStatusEnum.RESERVED.status -> TableStatusEnum.RESERVED
                else -> TableStatusEnum.AVAILABLE
            }
        }

        fun getCodeStatus(codeStatus: String): CodeStatusEnum {
            return when (codeStatus) {
                CodeStatusEnum.AVAILABLE.status -> CodeStatusEnum.AVAILABLE
                CodeStatusEnum.SUCCESSFULLY_TAKEN.status -> CodeStatusEnum.SUCCESSFULLY_TAKEN
                else -> CodeStatusEnum.AVAILABLE
            }
        }

        fun getEmployerRole(employerRole: String): TypeEmployeeRoleEnum {
            return when (employerRole) {
                TypeEmployeeRoleEnum.COOK.role -> TypeEmployeeRoleEnum.COOK
                TypeEmployeeRoleEnum.WAITER.role -> TypeEmployeeRoleEnum.WAITER
                else -> TypeEmployeeRoleEnum.WAITER
            }
        }

        fun getEmployerStatus(employerStatus: String): EmployerStatusEnum {
            return when (employerStatus) {
                EmployerStatusEnum.AVAILABLE.status -> EmployerStatusEnum.AVAILABLE
                EmployerStatusEnum.NOT_AVAILABLE.status -> EmployerStatusEnum.NOT_AVAILABLE
                else -> EmployerStatusEnum.AVAILABLE
            }
        }

        fun getTypeResource(type: String): TypeResourceEnum {
            return when (type) {
                TypeResourceEnum.FOOD.type -> TypeResourceEnum.FOOD
                TypeResourceEnum.PROTEIN.type -> TypeResourceEnum.PROTEIN
                TypeResourceEnum.DRINKS.type -> TypeResourceEnum.DRINKS
                TypeResourceEnum.COMMERCIAL_PRODUCTS.type -> TypeResourceEnum.COMMERCIAL_PRODUCTS
                TypeResourceEnum.COMPANION.type -> TypeResourceEnum.COMPANION
                else -> TypeResourceEnum.FOOD
            }
        }

        fun getStatusMenu(status: String): MenuStatusEnum {
            return when (status) {
                MenuStatusEnum.AVAILABLE.status -> MenuStatusEnum.AVAILABLE
                MenuStatusEnum.NOT_AVAILABLE.status -> MenuStatusEnum.NOT_AVAILABLE
                else -> MenuStatusEnum.AVAILABLE
            }
        }

        fun generateUniqueCode(): String {
            // Get the current time in milliseconds
            val currentTime = Date().time

            // Create a hash using a simple mathematical operation
            val hash = abs(currentTime.hashCode())

            // Convert the hash to a 4-digit string
            val shortHash = (hash % 10000).toString().padStart(4, '0')

            // Add a random 2-digit number
            val randomPart = Random.nextInt(10, 99)

            // Combine the hashed time and random part to get a 6-figure code
            return "$shortHash$randomPart"
        }
    }
}