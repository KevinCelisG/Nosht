package com.korlabs.nosht.domain.model

import com.korlabs.nosht.domain.model.enums.TableStatusEnum
import kotlinx.serialization.Serializable

data class Table(
    var name: String,
    var status: TableStatusEnum = TableStatusEnum.AVAILABLE,
    var documentReference: String? = null
)
