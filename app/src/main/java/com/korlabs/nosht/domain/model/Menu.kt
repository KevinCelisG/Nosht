package com.korlabs.nosht.domain.model

import com.korlabs.nosht.domain.model.enums.MenuStatusEnum

data class Menu(
    var name: String,
    var listResourceBusiness: List<ResourceWithAmountInMenu>,
    var menuStatusEnum: MenuStatusEnum = MenuStatusEnum.AVAILABLE,
    var price: Float,
    var isDynamic: Boolean = false,
    var documentReference: String? = null
)

data class ResourceWithAmountInMenu(
    val resourceBusiness: ResourceBusiness,
    var amount: Float
)

data class MenusWithAmountInOrder(
    val menu: Menu,
    var amount: Short
)