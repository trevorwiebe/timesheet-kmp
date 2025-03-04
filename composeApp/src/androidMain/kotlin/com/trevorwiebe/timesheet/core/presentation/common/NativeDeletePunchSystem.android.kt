package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
actual fun NativeDeletePunchDialog(
    modifier: Modifier,
    onDelete: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    NativeDestructiveButton(
        modifier = modifier,
        onClick = { showDialog = true },
        text = "Delete Time"
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Are you sure?") },
            text = { Text("Are you sure you want to delete this time? This will remove the punch-in and punch-out time.") },
            confirmButton = {
                Button(onClick = onDelete) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
