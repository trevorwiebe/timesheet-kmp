package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DeletePunchDialog(
    modifier: Modifier,
    visible: Boolean,
    onDelete: () -> Unit,
)