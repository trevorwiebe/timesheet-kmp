package com.trevorwiebe.timesheet.more.presentation.dialogsAndSheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
actual fun ConfirmSignOut(
    show: Boolean,
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
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red),
                            onClick = onConfirm,
                        ) {
                            Text(
                                text = "Yes"
                            )
                        }
                    }
                }
            }
        }
    }
}