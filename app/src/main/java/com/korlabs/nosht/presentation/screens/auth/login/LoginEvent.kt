package com.korlabs.nosht.presentation.screens.auth.login

sealed class LoginEvent {
    data class Login(val user: String, val password: String) : LoginEvent()
    data object IsLoggedEvent : LoginEvent()
}
