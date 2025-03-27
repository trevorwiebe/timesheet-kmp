package com.trevorwiebe.timesheet.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun TopBar(
    title: String,
    backIcon: @Composable() (() -> Unit?)? = null,
    onBack: () -> Unit = {},
    actions: @Composable() RowScope.() -> Unit = {},
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (backIcon != null) {
                IconButton(
                    onClick = onBack
                ) { backIcon() }
            }
            Text(
                modifier = Modifier.padding(16.dp),
                text = title,
                color = tertiary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            actions()
        }
        Divider()
    }
}