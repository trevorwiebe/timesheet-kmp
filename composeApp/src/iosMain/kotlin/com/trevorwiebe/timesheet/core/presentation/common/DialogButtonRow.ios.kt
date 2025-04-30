package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
actual fun DialogButtonRow(
    confirmButtonText: String?,
    onConfirmClick: () -> Unit,
    dismissButtonText: String?,
    onDismissClick: () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (confirmButtonText != null && dismissButtonText != null)
            Arrangement.SpaceAround else Arrangement.End
    ) {

        if (dismissButtonText != null) {
            TextButton(
                modifier = Modifier.padding(0.dp),
                onClick = onDismissClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFFFF0000)
                )
            ) {
                Text(
                    text = dismissButtonText,
                    fontSize = 16.sp,
                    letterSpacing = 0.sp,
                )
            }
        }

        if (confirmButtonText != null) {
            TextButton(
                modifier = Modifier.padding(0.dp),
                onClick = onConfirmClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF007AFF)
                )
            ) {
                Text(
                    text = confirmButtonText,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp,
                    fontSize = 16.sp
                )
            }
        }
    }
}