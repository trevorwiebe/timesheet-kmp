package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable

@Composable
actual fun NativeDialog(
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
    visible: Boolean,
    title: String?,
    message: String?,
) {
}