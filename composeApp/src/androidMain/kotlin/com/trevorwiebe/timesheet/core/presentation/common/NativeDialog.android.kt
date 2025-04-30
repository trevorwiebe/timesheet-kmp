package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

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

    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    if (title != null) {
                        Text(
                            fontSize = 16.sp,
                            text = title,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (message != null) {
                        Text(
                            fontSize = 14.sp,
                            text = message
                        )
                    }

                    DialogButtonRow(
                        confirmButtonText = confirmText,
                        onConfirmClick = onConfirm,
                        dismissButtonText = dismissText,
                        onDismissClick = onDismiss
                    )

                }
            }
        }
    }
}