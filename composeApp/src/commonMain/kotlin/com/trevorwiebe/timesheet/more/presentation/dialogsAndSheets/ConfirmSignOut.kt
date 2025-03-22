package com.trevorwiebe.timesheet.more.presentation.dialogsAndSheets

import androidx.compose.runtime.Composable

@Composable
expect fun ConfirmSignOut(
    show: Boolean,
    loadingSignOut: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
)