package com.korlabs.nosht.presentation.screens.auth.sign_up

import com.korlabs.nosht.data.remote.model.UserSignUp

sealed class SignUpEvent {
    data class SignUp(val userSignUp: UserSignUp) : SignUpEvent()
}
