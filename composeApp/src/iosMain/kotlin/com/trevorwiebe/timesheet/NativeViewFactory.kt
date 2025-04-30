package com.trevorwiebe.timesheet

import com.trevorwiebe.timesheet.core.domain.model.Punch
import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createTimePickerView(
        punch: Punch,
        onDismiss: () -> Unit,
        onTimeSelected: (Punch) -> Unit,
    ): UIViewController

    fun createDialog(
        confirmText: String,
        onConfirm: () -> Unit,
        dismissText: String,
        onDismiss: () -> Unit,
        title: String?,
        message: String?,
    ): UIViewController

}