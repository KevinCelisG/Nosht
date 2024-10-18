package com.korlabs.nosht.domain.model.users

import com.korlabs.nosht.domain.model.enums.TypeUserEnum

open class User(
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    var uid: String? = null,
    val typeUserEnum: TypeUserEnum
)
