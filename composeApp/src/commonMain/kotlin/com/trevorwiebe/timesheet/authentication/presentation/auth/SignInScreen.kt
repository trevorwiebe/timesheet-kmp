package com.trevorwiebe.timesheet.authentication.presentation.auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetButton
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetTextField
import com.trevorwiebe.timesheet.theme.tertiary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Timesheet",
                textAlign = TextAlign.Center,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(36.dp))

            TimesheetTextField(
                text = state.email,
                placeholder = "Email",
                onTextChange = {
                    viewModel.onEvent(
                        SignInEvents.OnEmailChange(it)
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TimesheetTextField(
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
                }
            )

            Text(
                modifier = Modifier
                    .padding(top=16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                text = state.signInError ?: "",
                color = Color.Red
            )

            TimesheetButton(
                text = "Sign In",
                loading = state.loadingSignIn,
                onClick = {
                    viewModel.onEvent(SignInEvents.OnSignInClick)
                }
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
            Card{
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