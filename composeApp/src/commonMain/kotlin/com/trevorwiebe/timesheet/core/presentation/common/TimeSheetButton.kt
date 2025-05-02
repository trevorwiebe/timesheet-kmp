package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun TimeSheetButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier,
    loading: Boolean,
    enabled: Boolean,
)