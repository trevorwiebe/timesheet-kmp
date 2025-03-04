package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun NativeDeletePunchDialog(
    modifier: Modifier,
    onDelete: () -> Unit,
)