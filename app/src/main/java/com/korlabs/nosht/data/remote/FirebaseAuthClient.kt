package com.korlabs.nosht.data.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.remote.AuthClient
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthClient @Inject constructor() : AuthClient {

    private val auth = Firebase.auth

    override suspend fun login(user: String, password: String): Resource<String> {
        return try {
            val response = auth.signInWithEmailAndPassword(user, password).await()
            Resource.Successful(data = response.user?.uid)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Error in the login with the business $user")
        }
    }

    override suspend fun signUp(userSignUp: UserSignUp): Resource<String> {
        return try {
            val response =
                auth.createUserWithEmailAndPassword(userSignUp.email, userSignUp.password).await()
            userSignUp.uid = response.user?.uid.toString()
            Log.d(Util.TAG, "Business SingUp $userSignUp")

            Log.d(Util.TAG, "UID ${response.user?.uid.toString()}")

            Log.d(Util.TAG, "Successful")
            Resource.Successful()
        } catch (e: Exception) {
            Log.d(Util.TAG, "Error ${e.message}")
            Resource.Error(
                message = e.message ?: "Error in the sign up with the business ${userSignUp.email}"
            )
        }
    }
}