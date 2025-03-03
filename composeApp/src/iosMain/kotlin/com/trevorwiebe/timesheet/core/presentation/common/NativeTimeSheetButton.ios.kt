package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.trevorwiebe.timesheet.LocalNativeViewFactory

@Composable
actual fun NativeTimeSheetButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier
) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = modifier,
        factory = {
            factory.createButtonView(
                text = text,
                onClick = onClick
            )
        }
    )
}