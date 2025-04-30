package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable

@Composable
expect fun NativeDialog(
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
    visible: Boolean,
    title: String?,
    message: String?,
)