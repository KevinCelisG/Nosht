package com.korlabs.nosht.domain.model.users

import com.korlabs.nosht.domain.model.enums.TypeUserEnum

class Business(
    name: String? = "",
    lastName: String? = "",
    email: String? = "",
    phone: String? = "",
    val location: String? = "",
    val businessName: String? = "",
    val isOpenTheBusiness: Boolean? = false,
    uid: String? = null,
    typeUserEnum: TypeUserEnum = TypeUserEnum.BUSINESS
) : User(
    name,
    lastName,
    email,
    phone,
    uid,
    typeUserEnum
)
