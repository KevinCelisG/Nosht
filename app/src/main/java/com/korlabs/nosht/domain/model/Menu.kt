package com.korlabs.nosht.domain.model

import com.korlabs.nosht.domain.model.enums.MenuStatusEnum

data class Menu(
    var name: String,
    var listResourceBusiness: List<ResourceWithAmountInMenu>,
    var menuStatusEnum: MenuStatusEnum,
    var price: Float,
    var isDynamic: Boolean,
    var documentReference: String? = null
)

data class ResourceWithAmountInMenu(
    val resourceBusiness: ResourceBusiness,
    val amount: Float
)