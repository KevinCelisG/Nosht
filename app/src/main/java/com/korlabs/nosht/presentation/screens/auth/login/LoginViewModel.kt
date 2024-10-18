package com.korlabs.nosht.presentation.screens.auth.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())

    fun onEvent(loginEvent: LoginEvent) {
        when (loginEvent) {
            is LoginEvent.Login -> {
                login(loginEvent.user, loginEvent.password)
            }

            is LoginEvent.IsLoggedEvent -> {
                isLogged()
            }
        }
    }

    private fun login(user: String, password: String) {
        viewModelScope.launch {
            repository.login(user, password).collect { result ->
                state = when (result) {
                    is Resource.Successful -> {
                        Log.d(Util.TAG, "Successful ${result.data}")
                        state.copy(user = result.data)
                    }

                    is Resource.Error -> {
                        Log.d(
                            Util.TAG,
                            result.message ?: "Error in the login with the business $user"
                        )
                        state.copy(user = null)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    private fun isLogged() {
        viewModelScope.launch {
            val userLogged = repository.getLoggedUser()
            state = state.copy(
                isLogged = userLogged != null,
                isProcessLoggedReady = true,
                user = userLogged
            )
        }
    }
}