package com.korlabs.nosht.data.remote.model

import com.korlabs.nosht.domain.model.enums.TypeUserEnum

data class UserSignUp(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val typeUserEnum: TypeUserEnum,
    val lastName: String? = "",
    var uid: String? = null
)