package com.korlabs.nosht.presentation.screens.auth.sign_up

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(SignUpState())

    fun onEvent(signUpEvent: SignUpEvent) {
        when (signUpEvent) {
            is SignUpEvent.SignUp -> {
                state = state.copy(isLoading = true)
                signUp(signUpEvent.userSignUp)
            }
        }
    }

    private fun signUp(userSignUp: UserSignUp) {
        viewModelScope.launch {
            repository.signUp(userSignUp).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Is Successful!!!")
                        state.copy(isSuccessfulSignUP = true)
                    }

                    is Resource.Error -> {
                        Log.d(Util.TAG, "Damn dude is an error. Error ${result.message}")
                        state.copy(isSuccessfulSignUP = false)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }
}