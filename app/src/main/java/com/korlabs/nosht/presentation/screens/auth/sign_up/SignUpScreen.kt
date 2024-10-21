package com.korlabs.nosht.presentation.screens.auth.sign_up

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.components.text_field.PasswordTextFieldCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.util.Util

@Composable
fun SignUpScreen(
    navHostController: NavController,
    args: Screen.SignUpScreen,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.state

    var name by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }

    var checkSignUp by rememberSaveable { mutableStateOf(false) }
    var processSignUp by rememberSaveable { mutableStateOf(false) }

    val title = stringResource(
        if (!args.isBusiness) {
            R.string.sign_up_title_employer
        } else {
            R.string.sign_up_title_business
        }
    )

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        TextTitleCustom(title = title)

        Spacer(modifier = Modifier.height(20.dp))

        TextFieldCustom(
            value = name,
            onValueChange = { name = it },
            hint = stringResource(id = R.string.hint_name)
        )

        if (!args.isBusiness) {
            Spacer(modifier = Modifier.height(20.dp))

            TextFieldCustom(
                value = lastName,
                onValueChange = { lastName = it },
                hint = stringResource(id = R.string.hint_last_name)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldCustom(
            value = phone,
            onValueChange = { phone = it },
            hint = stringResource(id = R.string.hint_phone)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextFieldCustom(
            value = email,
            onValueChange = { email = it },
            hint = stringResource(id = R.string.email_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextFieldCustom(
            value = password,
            onValueChange = { password = it },
            hint = stringResource(id = R.string.hint_password)
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextFieldCustom(
            value = passwordConfirm,
            onValueChange = { passwordConfirm = it },
            hint = stringResource(id = R.string.hint_password_confirm)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        ButtonCustom(text = stringResource(id = R.string.sign_up_button)) {
            checkSignUp = true
        }
    }

    LaunchedEffect(key1 = checkSignUp, block = {
        if (checkSignUp) {
            if (checkFields(args, name, phone, email, password, passwordConfirm, lastName)) {
                if (password == passwordConfirm) {
                    viewModel.onEvent(
                        SignUpEvent.SignUp(
                            UserSignUp(
                                name,
                                email,
                                phone,
                                password,
                                if (args.isBusiness) TypeUserEnum.BUSINESS else TypeUserEnum.EMPLOYER,
                                if (args.isBusiness) null else lastName,
                            )
                        )
                    )
                    processSignUp = true
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.passwords_do_not_match),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
            checkSignUp = false
        }
    })

    LaunchedEffect(key1 = state.isSuccessfulSignUP, block = {
        if (processSignUp) {
            if (state.isSuccessfulSignUP) {
                Toast.makeText(
                    context,
                    context.getString(R.string.sign_up_successful),
                    Toast.LENGTH_SHORT
                ).show()
                navHostController.navigateUp()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.sign_up_failed),
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(Util.TAG, "Failed in the screen, so weird. ${viewModel.state}")
            }
            processSignUp = false
        }
    })

    BackHandler {
        navHostController.navigateUp()
    }
}

fun checkFields(
    args: Screen.SignUpScreen,
    name: String,
    phone: String,
    email: String,
    password: String,
    passwordConfirm: String,
    lastName: String
): Boolean {
    val checkBasicsFields =
        name.isNotBlank() && phone.isNotBlank() && email.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()

    return if (!args.isBusiness) {
        checkBasicsFields && lastName.isNotBlank()
    } else {
        checkBasicsFields
    }
}
