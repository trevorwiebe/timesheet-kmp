package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.theme.onTertiary
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
actual fun TimeSheetButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier,
    loading: Boolean
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = tertiary,
        ),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        content = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = onTertiary
                    )
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = text,
                    color = onTertiary
                )
            }
        }
    )
}