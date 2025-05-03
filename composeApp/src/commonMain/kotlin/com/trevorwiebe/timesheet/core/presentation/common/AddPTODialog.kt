package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.LocalDate

@Composable
fun AddPTODialog(
    showDialog: Boolean,
    onConfirm: (date: LocalDate, hours: Int) -> Unit,
    onDismiss: () -> Unit,
) {

    val vacationHours = remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                vacationHours.value = ""
                onDismiss()
            },
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        fontSize = 20.sp,
                        text = "Add PTO",
                        fontWeight = FontWeight.Bold
                    )

                    TimesheetTextField(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = vacationHours.value,
                        placeholder = "PTO Hours",
                        onTextChange = {
                            vacationHours.value = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    OutlinedTextField(
                        placeholder = { Text(text = "Date") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        value = "5/1/2025",
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = tertiary,
                            unfocusedBorderColor = primary,
                            cursorColor = tertiary
                        ),
                        onValueChange = {}
                    )

                    DialogButtonRow(
                        confirmButtonText = "Save",
                        onConfirmClick = {
                            onConfirm(LocalDate.parse("5/1/2025"), vacationHours.value.toInt())
                        },
                        dismissButtonText = "Cancel",
                        onDismissClick = {
                            vacationHours.value = ""
                            onDismiss()
                        },
                    )
                }
            }
        }
    }

}