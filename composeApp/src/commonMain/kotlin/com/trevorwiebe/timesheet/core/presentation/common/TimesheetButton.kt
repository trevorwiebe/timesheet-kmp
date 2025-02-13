package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    text: String,
    loading: Boolean,
    onClick: () -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = tertiary,
            ),
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            content = {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = text,
                    color = onTertiary
                )
            }
        )
        if(loading){
            CircularProgressIndicator(color = onTertiary)
        }
    }
}