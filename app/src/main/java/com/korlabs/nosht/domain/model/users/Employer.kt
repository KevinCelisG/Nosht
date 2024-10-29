package com.korlabs.nosht.domain.model.users

import com.korlabs.nosht.domain.model.enums.TypeUserEnum

class Employer(
    name: String? = "",
    email: String? = "",
    phone: String? = "",
    lastName: String? = "",
    uid: String? = null,
    typeUserEnum: TypeUserEnum = TypeUserEnum.EMPLOYER
) : User(
    name,
    lastName,
    email,
    phone,
    uid,
    typeUserEnum
)
