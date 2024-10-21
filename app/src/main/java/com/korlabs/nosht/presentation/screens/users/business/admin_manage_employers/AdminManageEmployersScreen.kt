package com.korlabs.nosht.presentation.screens.users.business.admin_manage_employers

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.text.TextTitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.screens.users.employers.employer_home.ContractItem
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsEvent
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsViewModel
import com.korlabs.nosht.presentation.screens.users.general.timer.TimerDisplay
import com.korlabs.nosht.presentation.screens.users.general.timer.TimerViewModel
import com.korlabs.nosht.util.Util
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManageEmployersScreen(
    navHostController: NavHostController,
    //employersViewModel: EmployersViewModel,
    contractsViewModel: ContractsViewModel,
    timerViewModel: TimerViewModel
) {
    val state = contractsViewModel.state
    val context = LocalContext.current

    var showAddEmployerDialog by remember { mutableStateOf(false) }
    var createCodeToAddEmployer by remember { mutableStateOf(false) }

    var selectedRoleEmployer by remember { mutableStateOf(TypeEmployeeRoleEnum.WAITER) }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf(TypeEmployeeRoleEnum.WAITER.role, TypeEmployeeRoleEnum.COOK.role)

    var processCreateCode by remember { mutableStateOf(false) }

    var showCountDownTimeDialog by remember { mutableStateOf(false) }

    var isButtonCreateCodeEnabled by remember { mutableStateOf(true) }

    var listenEmployerResponse by rememberSaveable { mutableStateOf(false) }

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.employers_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(5.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            TextFieldCustom(
                value = "",
                onValueChange = {

                },
                hint = stringResource(id = R.string.enter_employee_name)
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(5.dp)
            ) {
                items(state.listContracts.size) {
                    // TO DO
                    ContractItem(state.listContracts[it], {
                        println("TO DO")
                    })
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(text = stringResource(id = R.string.add_employer)) {
            showAddEmployerDialog = true
        }

        if (showAddEmployerDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.add_new_employer), fontSize = 20.sp)
                },
                text = {
                    Column {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                readOnly = true,
                                value = selectedRoleEmployer.role,
                                onValueChange = { },
                                label = { Text(stringResource(id = R.string.select_employer_role)) },
                                trailingIcon = {
                                    TrailingIcon(
                                        expanded = expanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                roles.forEach { role ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = role)
                                        },
                                        onClick = {
                                            selectedRoleEmployer = Util.getEmployerRole(role)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (isButtonCreateCodeEnabled) {
                                Log.d(Util.TAG, "Create an user")
                                isButtonCreateCodeEnabled = false
                                createCodeToAddEmployer = true
                            }
                        },
                        enabled = isButtonCreateCodeEnabled
                    ) {
                        Text(stringResource(id = R.string.add))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showAddEmployerDialog = false }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }

        if (showCountDownTimeDialog) {
            Log.d(Util.TAG, "Open de dialog for timer")

            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.count_down_code), fontSize = 20.sp)
                },
                text = {
                    val timeLeft by timerViewModel.timeLeft.collectAsState()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            //.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = state.code!!,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (timeLeft == 0) {
                            LaunchedEffect(Unit) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.timer_finished),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            contractsViewModel.onEvent(ContractsEvent.DisabilityCode)
                            showCountDownTimeDialog = false
                        }

                        TimerDisplay(timeLeft)
                    }
                },
                confirmButton = {

                },
                dismissButton = {
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.cancel_timer),
                                Toast.LENGTH_SHORT
                            ).show()
                            contractsViewModel.onEvent(ContractsEvent.DisabilityCode)
                            showCountDownTimeDialog = false
                            timerViewModel.finishTimer()
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }
    }

    LaunchedEffect(key1 = createCodeToAddEmployer) {
        if (createCodeToAddEmployer) {
            if (selectedRoleEmployer != null) {
                Log.d(Util.TAG, "Launch the code")
                contractsViewModel.onEvent(ContractsEvent.Add(selectedRoleEmployer!!))
                processCreateCode = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT
                )
                    .show()
                isButtonCreateCodeEnabled = true
            }
            createCodeToAddEmployer = false
        }
    }

    LaunchedEffect(key1 = state.code) {
        if (processCreateCode) {
            if (!state.code.isNullOrBlank()) {
                Log.d(Util.TAG, "Code successful ${state.code}")

                listenEmployerResponse = true
                contractsViewModel.onEvent(ContractsEvent.ListenEmployerResponse)

                Log.d(Util.TAG, "Start with timer")
                timerViewModel.startTimer()

                showCountDownTimeDialog = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_adding_employer),
                    Toast.LENGTH_SHORT
                ).show()
            }
            showAddEmployerDialog = false
            isButtonCreateCodeEnabled = true
            processCreateCode = false
        }
    }

    LaunchedEffect(key1 = state.isTakenCode) {
        if (listenEmployerResponse) {
            if (state.isTakenCode != null) {
                if (state.isTakenCode) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.employer_joined_successfully),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    contractsViewModel.onEvent(ContractsEvent.DisabilityCode)
                    timerViewModel.finishTimer()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.employer_taken_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                showCountDownTimeDialog = false
                listenEmployerResponse = false
            }
        }
    }

    LaunchedEffect(key1 = contractsViewModel.isNewRemoteContracts) {
        if (contractsViewModel.isNewRemoteContracts) {
            Log.d(Util.TAG, "There are a new remote data contracts")
            contractsViewModel.onEvent(ContractsEvent.GetLocalContracts)
        }
    }

    LaunchedEffect(Unit) {
        contractsViewModel.onEvent(ContractsEvent.GetLocalContracts)
    }

    BackHandler {
        navHostController.navigate(Screen.LoginScreen)
    }
}
