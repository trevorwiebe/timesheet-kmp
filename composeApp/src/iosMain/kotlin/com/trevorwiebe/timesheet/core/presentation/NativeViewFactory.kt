package com.trevorwiebe.timesheet.core.presentation

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createButtonView(
        text: String,
        onClick: () -> Unit
    ): UIViewController

    fun createDestructionButton(
        text: String,
        onClick: () -> Unit
    ): UIViewController
}