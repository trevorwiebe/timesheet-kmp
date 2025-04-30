package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

        UIKitViewController(
            modifier = Modifier,
            factory = {
                factory.createTimePickerView(punch, onDismiss, onTimeSelected)
            }
        )
    }
}