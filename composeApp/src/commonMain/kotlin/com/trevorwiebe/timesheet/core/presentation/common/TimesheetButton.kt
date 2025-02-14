package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun TimesheetButton(
    modifier: Modifier,
    text: String,
    loading: Boolean,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            }
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
        if(loading){
            CircularProgressIndicator(color = onTertiary)
        }
    }
}