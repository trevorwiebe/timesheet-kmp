package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
actual fun DialogButtonRow(
    confirmButtonText: String?,
    onConfirmClick: () -> Unit,
    dismissButtonText: String?,
    onDismissClick: () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Spacer(modifier = Modifier.weight(1f))

        if (dismissButtonText != null) {
            Button(
                onClick = onDismissClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Text(dismissButtonText)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        if (confirmButtonText != null) {
            Button(
                onClick = onConfirmClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = tertiary,
                    contentColor = Color.White
                )
            ) {
                Text(confirmButtonText)
            }
        }
    }
}