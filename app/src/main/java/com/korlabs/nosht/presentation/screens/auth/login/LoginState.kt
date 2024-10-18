package com.korlabs.nosht.presentation.screens.auth.login

import com.korlabs.nosht.domain.model.users.User

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val user: User? = null,
    val isLogged: Boolean = false,
    val isProcessLoggedReady: Boolean = false
)
