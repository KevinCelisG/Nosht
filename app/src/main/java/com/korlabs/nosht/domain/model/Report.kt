package com.korlabs.nosht.domain.model

import com.korlabs.nosht.domain.model.enums.TimeEnum

data class Report(
    var timeEnum: TimeEnum,
    var bestWaiter: Map.Entry<String, Int>?,
    var bestMenu: Map.Entry<Menu, Int>?,
    val bestResource: Map.Entry<ResourceBusiness, Int>?,
    var revenue: Float,
    var profit: Float,
    var totalOrders: Short
)