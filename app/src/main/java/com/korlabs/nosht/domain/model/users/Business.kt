package com.korlabs.nosht.domain.model.users

import com.korlabs.nosht.domain.model.enums.TypeUserEnum

class Business(
    name: String? = "",
    email: String? = "",
    phone: String? = "",
    val direction: String? = "",
    uid: String? = null,
    typeUserEnum: TypeUserEnum = TypeUserEnum.BUSINESS
) : User(
    name,
    email,
    phone,
    uid,
    typeUserEnum
)
