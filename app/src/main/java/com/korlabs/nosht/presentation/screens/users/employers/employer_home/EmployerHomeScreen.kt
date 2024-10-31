package com.korlabs.nosht.presentation.screens.users.employers.employer_home

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.korlabs.nosht.R
import com.korlabs.nosht.data.repository.AuthRepositoryImpl
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.navigation.Screen
import com.korlabs.nosht.presentation.components.button.ButtonCustom
import com.korlabs.nosht.presentation.components.column.ColumnCustom
import com.korlabs.nosht.presentation.components.text.TextSubtitleCustom
import com.korlabs.nosht.presentation.components.text_field.TextFieldCustom
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsEvent
import com.korlabs.nosht.presentation.screens.users.general.contracts.ContractsViewModel
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesEvent
import com.korlabs.nosht.presentation.screens.users.general.tables.TablesViewModel
import com.korlabs.nosht.util.Util

@Composable
fun EmployerHomeScreen(
    navHostController: NavHostController,
    //manageEmployersViewModel: EmployersViewModel,
    tablesViewModel: TablesViewModel,
    contractsViewModel: ContractsViewModel
) {
    val context = LocalContext.current

    var launchJoinBusinessDialog by rememberSaveable { mutableStateOf(false) }
    var codeBusiness by rememberSaveable { mutableStateOf("") }
    var reviewCodeBusiness by rememberSaveable { mutableStateOf(false) }
    var processCodeBusiness by rememberSaveable { mutableStateOf(false) }

    val state = contractsViewModel.state

    ColumnCustom {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.contracts_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
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
                hint = stringResource(id = R.string.enter_contract)
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
                    ContractItem(state.listContracts[it], { contract ->
                        // TO DO FOR COOK and add option wo show the performance in a particular not available business
                        Log.d(
                            Util.TAG,
                            "Click on ${contract.role} - ${contract.userUid} - ${contract.status}"
                        )

                        AuthRepositoryImpl.currentBusinessUid = contract.userUid

                        Log.d(Util.TAG, "Using the current business uid ${contract.userUid}")

                        if (contract.role == TypeEmployeeRoleEnum.WAITER) {
                            if (contract.status == EmployerStatusEnum.AVAILABLE) {
                                tablesViewModel.onEvent(TablesEvent.GetRemoteTables)
                                navHostController.navigate(Screen.WaiterScreen)
                            }
                        }
                    })
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(text = stringResource(id = R.string.join_to_business)) {
            launchJoinBusinessDialog = true
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (launchJoinBusinessDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = stringResource(id = R.string.join_to_business), fontSize = 20.sp)
                },
                text = {
                    OutlinedTextField(
                        value = codeBusiness,
                        onValueChange = { codeBusiness = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        placeholder = {
                            Text(text = stringResource(id = R.string.enter_restaurant_code))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            reviewCodeBusiness = true
                        }
                    ) {
                        Text(stringResource(id = R.string.add))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { launchJoinBusinessDialog = false }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            )
        }

        LaunchedEffect(key1 = reviewCodeBusiness, block = {
            if (reviewCodeBusiness) {
                if (codeBusiness.isNotBlank()) {
                    contractsViewModel.onEvent(ContractsEvent.ValidateCode(codeBusiness))
                    processCodeBusiness = true
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.fill_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                reviewCodeBusiness = false
            }
        })

        LaunchedEffect(key1 = state.isTakenCode, block = {
            if (processCodeBusiness) {
                if (state.isTakenCode != null) {
                    if (state.isTakenCode) {
                        //tablesViewModel.onEvent(TablesEvent.GetRemoteTables)

                        Toast.makeText(
                            context,
                            context.getString(R.string.joined_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.joined_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    launchJoinBusinessDialog = false
                    processCodeBusiness = false
                }
            }
        })
    }

    LaunchedEffect(key1 = state.isNewRemoteContracts) {
        if (state.isNewRemoteContracts) {
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

@Composable
fun ContractItem(
    contract: Contract,
    onClick: (Contract) -> Unit,
    modifier: Modifier = Modifier
) {
    val color =
        colorResource(id = if (contract.status == EmployerStatusEnum.AVAILABLE) R.color.dark_green else R.color.dark_red)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.9f)
            .background(color, shape = RoundedCornerShape(16.dp))
            .clickable {
                onClick(contract)
            }
    ) {
        TextSubtitleCustom(subtitle = "${contract.status} - ${contract.userUid}")
    }
}