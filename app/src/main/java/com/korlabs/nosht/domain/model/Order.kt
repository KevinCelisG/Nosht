package com.korlabs.nosht.domain.model

import com.google.firebase.firestore.DocumentReference
import com.korlabs.nosht.domain.model.enums.MenuStatusEnum
import com.korlabs.nosht.domain.model.enums.OrderStatusEnum
import com.korlabs.nosht.domain.model.enums.PayMethodsEnum
import java.time.LocalDate

data class Order(
    var resourcesAdditional: List<ResourceWithAmountInMenu>,
    var menus: List<MenusWithAmountInOrder>,
    var idTable: String,
    var idWaiter: String,
    var status: OrderStatusEnum,
    var date: LocalDate,
    var total: Float,
    var comments: String,
    var idChef: String = "",
    var payMethod: PayMethodsEnum = PayMethodsEnum.CASH,
    var documentReference: String? = null,
    // var tip (Other version)
)