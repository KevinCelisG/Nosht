package com.korlabs.nosht.data.mapper

import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.model.users.Employer

/*fun AuthResult.toUser(): Business {
    return Business(
        uid = user?.uid
    )
}*/

fun DocumentSnapshot.toBusiness(uid: String): Business {
    return Business(
        name = getString("name"),
        lastName = getString("lastName"),
        email = getString("email"),
        phone = getString("phone"),
        location = getString("location"),
        businessName = getString("businessName"),
        isOpenTheBusiness = getBoolean("isOpenTheBusiness"),
        uid = uid
    )
}

fun DocumentSnapshot.toEmployer(
    uid: String
): Employer {
    return Employer(
        name = getString("name"),
        email = getString("email"),
        phone = getString("phone"),
        lastName = getString("lastName"),
        uid = uid
    )
}