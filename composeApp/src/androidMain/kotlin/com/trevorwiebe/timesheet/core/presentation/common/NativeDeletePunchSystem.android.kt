package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
actual fun DeletePunchDialog(
    modifier: Modifier,
    onDelete: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    DestructiveButton(
        modifier = modifier,
        onClick = { showDialog = true },
        text = "Delete Time"
    )

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        fontSize = 20.sp,
                        text = "Are you sure?",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Are you sure you want to delete this time? This will remove the punch-in and punch-out time."
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = tertiary),
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red),
                            onClick = {
                                showDialog = false
                                onDelete()
                            },
                        ) {
                            Text(
                                text = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
}
