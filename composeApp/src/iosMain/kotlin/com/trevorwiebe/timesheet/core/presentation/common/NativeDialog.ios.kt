package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.trevorwiebe.timesheet.LocalNativeViewFactory

@Composable
actual fun NativeDialog(
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
    visible: Boolean,
    title: String?,
    message: String?,
) {

    if (visible) {
        val factory = LocalNativeViewFactory.current

        UIKitViewController(
            modifier = Modifier,
            factory = {
                factory.createDialog(
                    confirmText = confirmText,
                    onConfirm = onConfirm,
                    dismissText = dismissText,
                    onDismiss = onDismiss,
                    title = title,
                    message = message
                )
            }
        )
    }
}