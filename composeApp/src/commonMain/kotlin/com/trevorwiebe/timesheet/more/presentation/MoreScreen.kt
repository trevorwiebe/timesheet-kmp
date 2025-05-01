package com.trevorwiebe.timesheet.more.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.presentation.TopBar
import com.trevorwiebe.timesheet.core.presentation.common.NativeDialog
import com.trevorwiebe.timesheet.theme.secondary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = koinViewModel(),
    onSignOut: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.onSignOutChannel) {
        viewModel.onSignOutChannel.collect {
            onSignOut()
        }
    }

    Scaffold(
        topBar = { TopBar("More") }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ItemRow(
                primaryText = state.currentUser?.name ?: "",
                secondaryText = state.currentUser?.email ?: ""
            )
            ItemRow(
                primaryText = "Hire Date",
                secondaryText = Util.toFriendlyDate(state.currentUser?.hireDate)
            )
            ItemRow(
                primaryText = "Current PTO Balance",
                secondaryText = if (state.currentUser?.ptoBalance == 1)
                    "${state.currentUser?.ptoBalance} day"
                else "${state.currentUser?.ptoBalance} days"
            )
            ItemRow(
                primaryText = "Sign Out",
                onClick = { viewModel.onEvent(MoreEvents.OnShowConfirmSignOutSheet(true)) },
                clickable = true
            )
        }

        NativeDialog(
            visible = state.confirmSignOutSheet,
            title = "Sign Out",
            message = "Are you sure you want to sign out?",
            onDismiss = { viewModel.onEvent(MoreEvents.OnShowConfirmSignOutSheet(false)) },
            dismissText = "Cancel",
            onConfirm = { viewModel.onEvent(MoreEvents.OnSignOut) },
            confirmText = "Sign Out"
        )
    }
}

@Composable
private fun ItemRow(
    primaryText: String,
    secondaryText: String? = null,
    clickable: Boolean = false,
    onClick: () -> Unit = { },
) {
    Column(
        modifier = if (clickable) {
            Modifier.fillMaxWidth().clickable { onClick() }
        } else {
            Modifier.fillMaxWidth()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = if (secondaryText == null) 16.dp else 4.dp
                )
        ) {
            Text(
                fontSize = 16.sp,
                text = primaryText,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (secondaryText != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = secondaryText,
                    color = secondary
                )
            }
        }
        Divider()
    }
}