package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
actual fun DestructiveButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(bottom = 2.dp, top = 2.dp)
            .border(2.dp, Color(230, 74, 25), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .background(Color.White)
            .fillMaxWidth()
            .padding(4.dp),
        textAlign = TextAlign.Center,
        text = text,
        color = Color(230, 74, 25),
    )
}