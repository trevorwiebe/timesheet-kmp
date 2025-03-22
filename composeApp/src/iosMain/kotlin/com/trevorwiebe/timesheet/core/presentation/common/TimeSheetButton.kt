package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
actual fun TimeSheetButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier,
    loading: Boolean
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.padding(0.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = tertiary,
            backgroundColor = primary
        ),
        shape = CircleShape,
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.5).sp,
            fontSize = 16.sp
        )
    }
}