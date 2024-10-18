package com.korlabs.nosht.domain.remote

import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.util.Resource


interface AuthClient {

    suspend fun login(user: String, password: String): Resource<String>

    suspend fun signUp(userSignUp: UserSignUp): Resource<String>
}