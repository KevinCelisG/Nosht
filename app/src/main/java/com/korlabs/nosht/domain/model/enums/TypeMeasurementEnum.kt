package com.korlabs.nosht.domain.model.enums

import com.korlabs.nosht.NoshtApplication
import com.korlabs.nosht.R

enum class TypeMeasurementEnum(val type: String) {
    KILOGRAM(NoshtApplication.appContext.getString(R.string.kilograms_measurement)),
    LITER(NoshtApplication.appContext.getString(R.string.liters_measurement)),
    UNIT(NoshtApplication.appContext.getString(R.string.units_measurement))
}