package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.theme.primary

@Composable
actual fun DestructiveButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(bottom = 2.dp, top = 2.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(primary)
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        textAlign = TextAlign.Center,
        text = text,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.5).sp,
        fontSize = 16.sp,
        color = Color(230, 74, 25),
    )
}