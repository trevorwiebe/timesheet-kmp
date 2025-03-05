package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.theme.onTertiary
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
actual fun TimeSheetButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = tertiary,
        ),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        content = {
            Text(
                modifier = Modifier.padding(8.dp),
                text = text,
                color = onTertiary
            )
        }
    )
}