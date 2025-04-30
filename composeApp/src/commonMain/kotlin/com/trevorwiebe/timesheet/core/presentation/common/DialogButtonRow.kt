package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable

@Composable
expect fun DialogButtonRow(
    confirmButtonText: String? = null,
    onConfirmClick: () -> Unit = {},
    dismissButtonText: String? = null,
    onDismissClick: () -> Unit = {},
)