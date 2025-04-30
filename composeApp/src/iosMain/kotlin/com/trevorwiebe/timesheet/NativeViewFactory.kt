package com.trevorwiebe.timesheet

import com.trevorwiebe.timesheet.core.domain.model.Punch
import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createTimePickerView(
        punch: Punch,
        onDismiss: () -> Unit,
        onTimeSelected: (Punch) -> Unit,
    ): UIViewController
}