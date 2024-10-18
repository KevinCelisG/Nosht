package com.korlabs.nosht.domain.repository

import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.users.User
import com.korlabs.nosht.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(user: String, password: String): Flow<Resource<User>>

    suspend fun signUp(userSignUp: UserSignUp): Flow<Resource<String>>

    suspend fun getLoggedUser(): User?
}