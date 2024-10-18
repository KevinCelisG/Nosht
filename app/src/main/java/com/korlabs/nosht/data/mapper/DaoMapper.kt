package com.korlabs.nosht.data.mapper

import androidx.room.PrimaryKey
import com.korlabs.nosht.data.local.entities.BusinessEntity
import com.korlabs.nosht.data.local.entities.ContractEntity
import com.korlabs.nosht.data.local.entities.EmployerEntity
import com.korlabs.nosht.data.local.entities.MenuEntity
import com.korlabs.nosht.data.local.entities.ResourceEntity
import com.korlabs.nosht.data.local.entities.TableEntity
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.MenuStatusEnum
import com.korlabs.nosht.domain.model.users.Employer
import com.korlabs.nosht.util.Util
import com.korlabs.nosht.util.Util.Companion.getEmployerRole
import com.korlabs.nosht.util.Util.Companion.getEmployerStatus
import com.korlabs.nosht.util.Util.Companion.getTableStatus

fun Business.toBusinessEntity(): BusinessEntity {
    return BusinessEntity(
        id = uid ?: "",
        name = name ?: "",
        email = email ?: "",
        phone = phone ?: ""
    )
}

fun BusinessEntity.toBusiness(): Business {
    return Business(
        name = name,
        email = email,
        phone = phone,
        uid = id
    )
}

fun Employer.toEmployerEntity(): EmployerEntity {
    return EmployerEntity(
        id = uid ?: "",
        name = name ?: "",
        lastName = lastName ?: "",
        email = email ?: "",
        phone = phone ?: ""
    )
}

fun EmployerEntity.toEmployer(): Employer {
    return Employer(
        name = name,
        email = email,
        phone = phone,
        lastName = lastName,
        uid = id
    )
}

fun Table.toTableEntity(uid: String): TableEntity {
    return TableEntity(
        id = documentReference.toString(),
        userId = uid,
        name = name,
        status = status.status
    )
}

fun TableEntity.toTable(): Table {
    return Table(
        documentReference = id,
        name = name,
        status = getTableStatus(status)
    )
}

fun Contract.toContractEntity(uid: String): ContractEntity {
    return ContractEntity(
        id = documentReference.toString(),
        userId = uid,
        role = role.role,
        status = status.status,
        userUid = userUid
    )
}

fun ContractEntity.toContract(): Contract {
    return Contract(
        userUid = userUid,
        role = getEmployerRole(role),
        status = getEmployerStatus(status),
        documentReference = id
    )
}

fun ResourceBusiness.toResourceEntity(uid: String): ResourceEntity {
    return ResourceEntity(
        id = documentReference.toString(),
        userId = uid,
        name = name,
        type = typeResourceEnum.type
    )
}

fun ResourceEntity.toResourceBusiness(): ResourceBusiness {
    return ResourceBusiness(
        name = name,
        typeResourceEnum = Util.getTypeResource(type),
        documentReference = id
    )
}

fun Menu.toMenuEntity(uid: String): MenuEntity {
    return MenuEntity(
        id = documentReference.toString(),
        userId = uid,
        name = name,
        status = menuStatusEnum.status,
        price = price,
        isDynamic = isDynamic
    )
}

fun MenuEntity.toMenu(): Menu {
    return Menu(
        name = name,
        listResourceBusiness = emptyList(),
        menuStatusEnum = Util.getStatusMenu(status),
        price = price,
        isDynamic = isDynamic,
        documentReference = id
    )
}