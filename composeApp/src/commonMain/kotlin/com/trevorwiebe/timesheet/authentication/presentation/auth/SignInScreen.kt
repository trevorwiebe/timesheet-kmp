package com.trevorwiebe.timesheet.authentication.presentation.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetButton
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetTextField
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
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                onTextChange = {
                    viewModel.onEvent(
                        SignInEvents.OnPasswordChange(it)
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TimesheetButton(
                text = "Sign In",
                onClick = {  }
            )
        }
    }
}