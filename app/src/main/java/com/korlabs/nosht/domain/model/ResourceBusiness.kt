package com.korlabs.nosht.domain.model

import com.korlabs.nosht.domain.model.enums.TypeResourceEnum

data class ResourceBusiness(
    var name: String,
    var typeResourceEnum: TypeResourceEnum,
    var documentReference: String? = null
)
