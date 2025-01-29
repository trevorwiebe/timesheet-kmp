package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimesheetTextField(
    text: String,
    placeholder: String,
    onTextChange: (String) -> Unit,
) {

    OutlinedTextField(
        placeholder = { Text(text = placeholder) },
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = onTextChange
    )
}