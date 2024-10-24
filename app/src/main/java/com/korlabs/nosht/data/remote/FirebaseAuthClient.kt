package com.korlabs.nosht.data.remote

import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.korlabs.nosht.NoshtApplication
import com.korlabs.nosht.R
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
        } catch (e: FirebaseAuthUserCollisionException) {
            Resource.Error(message = NoshtApplication.appContext.getString(R.string.sign_up_error_exists_an_account))
        } catch (e: FirebaseNetworkException) {
            Resource.Error(message = NoshtApplication.appContext.getString(R.string.network_error))
        } catch (e: FirebaseAuthWeakPasswordException) {
            Resource.Error(message = NoshtApplication.appContext.getString(R.string.password_characters_error))
        } catch (e: Exception) {
            Resource.Error(message = NoshtApplication.appContext.getString(R.string.unrecognized_error))
        }
    }
}