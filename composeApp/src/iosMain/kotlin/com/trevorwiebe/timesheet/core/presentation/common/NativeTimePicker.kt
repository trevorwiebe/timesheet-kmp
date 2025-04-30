package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import com.trevorwiebe.timesheet.LocalNativeViewFactory
import com.trevorwiebe.timesheet.core.domain.model.Punch

@Composable
actual fun NativeTimePicker(
    punch: Punch?,
    onDismiss: () -> Unit,
    onTimeSelected: (Punch) -> Unit,
) {

    if (punch != null) {
        val factory = LocalNativeViewFactory.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // shadowed background
                .clickable(onClick = onDismiss) // dismiss on outside tap
        ) {
            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.Center) // Or custom offset if contextual
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .width(300.dp)
                    .height(280.dp)
                    .clickable(enabled = false) {} // prevent propagation
            ) {
                UIKitViewController(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        factory.createTimePickerView(punch, onDismiss, onTimeSelected)
                    }
                )
            }
        }
    }
}