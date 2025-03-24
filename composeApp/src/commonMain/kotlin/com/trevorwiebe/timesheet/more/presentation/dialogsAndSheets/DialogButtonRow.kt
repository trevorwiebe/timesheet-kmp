package com.trevorwiebe.timesheet.more.presentation.dialogsAndSheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun DialogButtonRow(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    actionLoading: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            colors = ButtonDefaults.textButtonColors(contentColor = tertiary),
            onClick = onDismiss
        ) {
            Text("Cancel")
        }
        Spacer(modifier = Modifier.width(16.dp))
        TextButton(
            colors = ButtonDefaults.textButtonColors(contentColor = tertiary),
            onClick = onConfirm,
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (actionLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                    )
                }
                Text(
                    text = "Yes"
                )
            }
        }
    }
}