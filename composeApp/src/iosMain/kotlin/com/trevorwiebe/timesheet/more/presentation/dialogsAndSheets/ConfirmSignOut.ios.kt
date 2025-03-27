package com.trevorwiebe.timesheet.more.presentation.dialogsAndSheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
actual fun ConfirmSignOut(
    show: Boolean,
    loadingSignOut: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    if (show) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        fontSize = 20.sp,
                        text = "Are you sure you want to sign out?",
                        fontWeight = FontWeight.Bold
                    )
                    DialogButtonRow(
                        onDismissText = "Cancel",
                        onDismiss = onDismiss,
                        onConfirmText = "Yes",
                        onConfirm = onConfirm,
                        actionLoading = loadingSignOut
                    )
                }
            }
        }
    }
}