package com.trevorwiebe.timesheet.signin.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trevorwiebe.timesheet.core.presentation.common.TimeSheetButton
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetTextField
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel(),
    onSignInSuccessful: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.onSignInSuccessful){
        viewModel.onSignInSuccessful.collect{
            // navigate to home screen
            onSignInSuccessful()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Sante Fe Carriers",
                textAlign = TextAlign.Center,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 48.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            TimesheetTextField(
                modifier = Modifier.focusRequester(focusRequester1),
                text = state.email,
                placeholder = "Email",
                onTextChange = {
                    viewModel.onEvent(
                        SignInEvents.OnEmailChange(it)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        coroutineScope.launch {
                            focusRequester2.requestFocus()
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            TimesheetTextField(
                modifier = Modifier.focusRequester(focusRequester2),
                text = state.password,
                placeholder = "Password",
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                hidePasswordText = state.hidePasswordText,
                onTextChange = {
                    viewModel.onEvent(
                        SignInEvents.OnPasswordChange(it)
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.onEvent(SignInEvents.OnSignInClick)
                    }
                )
            )

            Text(
                modifier = Modifier
                    .padding(top=16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                text = state.signInError ?: "",
                color = Color.Red
            )

            TimeSheetButton(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                text = "Sign In",
                onClick = {
                    viewModel.onEvent(SignInEvents.OnSignInClick)
                },
                loading = state.loadingSignIn,
                enabled = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .padding(top=16.dp, bottom = 16.dp)
                    .clickable {
                        viewModel.onEvent(SignInEvents.OnSetSendPasswordResetDialog(true))
                    },
                text = "Forgot Password?",
                color = tertiary
            )
        }
    }

    if(state.showSendPasswordResetDialog){
        Dialog(
            onDismissRequest = {
                viewModel.onEvent(SignInEvents.OnSetSendPasswordResetDialog(false))
            }
        ){
            Card { //todo Make sure the border radius matches other dialogs
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(
                        modifier = Modifier.padding(top=16.dp),
                        text = "Please enter your email address to reset your password",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TimesheetTextField(
                        text = state.email,
                        placeholder = "Email",
                        onTextChange = {
                            viewModel.onEvent(
                                SignInEvents.OnEmailChange(it)
                            )
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
                            onClick = {
                                viewModel.onEvent(
                                    SignInEvents.OnSetSendPasswordResetDialog(false)
                                )
                            },
                            colors = androidx.compose.material.ButtonDefaults.buttonColors(
                                contentColor = tertiary,
                                backgroundColor = Color.White
                            )
                        ) {
                            Text(text = "Cancel")
                        }
                        Box(
                            contentAlignment = Alignment.Center
                        ){
                            TextButton(
                                modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp),
                                onClick = {
                                    viewModel.onEvent(SignInEvents.OnSendPasswordResetEmail)
                                },
                                colors = androidx.compose.material.ButtonDefaults.buttonColors(
                                    contentColor = tertiary,
                                    backgroundColor = Color.White
                                )
                            ) {
                                Text(text = "Send reset email")
                            }
                            if(state.loadingPasswordReset){
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = tertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}