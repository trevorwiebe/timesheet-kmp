package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.theme.onTertiary
import com.trevorwiebe.timesheet.theme.tertiary
import com.trevorwiebe.timesheet.theme.tertiaryDisabled

@Composable
actual fun TimeSheetButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier,
    loading: Boolean,
    enabled: Boolean,
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = tertiary,
            disabledContainerColor = tertiaryDisabled
        ),
        modifier = modifier,
        shape = CircleShape,
        enabled = enabled,
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