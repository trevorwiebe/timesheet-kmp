package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.trevorwiebe.timesheet.LocalNativeViewFactory

@Composable
actual fun NativeDestructiveButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = modifier,
        factory = {
            factory.createDestructionButton(
                text = text,
                onClick = onClick
            )
        }
    )
}