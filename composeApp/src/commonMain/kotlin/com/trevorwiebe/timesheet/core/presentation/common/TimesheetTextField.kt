package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun TimesheetTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    hidePasswordText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextChange: (String) -> Unit,
) {

    OutlinedTextField(
        placeholder = { Text(text = placeholder) },
        modifier = modifier.fillMaxWidth(),
        value = text,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = tertiary,
            unfocusedBorderColor = primary,
            cursorColor = tertiary
        ),
        onValueChange = onTextChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = if (hidePasswordText) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}