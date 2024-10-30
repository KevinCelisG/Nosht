package com.korlabs.nosht.domain.model

import com.korlabs.nosht.domain.model.enums.TypeMeasurementEnum
import com.korlabs.nosht.domain.model.enums.TypeMovementEnum
import com.korlabs.nosht.domain.model.enums.TypeResourceEnum
import java.time.LocalDate
import java.time.temporal.TemporalAmount
import java.util.Date

/*
Amount if a float due that if I use a resource as a ingredient, the inventory have to rest this amount used in the menu
*/
data class ResourceBusiness(
    var name: String,
    var minStock: Short,
    var maxStock: Short,
    var price: Float,
    var amount: Float,
    var typeResourceEnum: TypeResourceEnum,
    var typeMeasurementEnum: TypeMeasurementEnum,
    var listMovements: List<ResourceMovement> = emptyList(),
    var documentReference: String? = null,
)

data class ResourceMovement(
    var date: LocalDate,
    var amount: Float,
    var price: Float,
    var typeMovement: TypeMovementEnum
)