package com.trevorwiebe.timesheet

import androidx.compose.ui.window.ComposeUIViewController
import com.trevorwiebe.timesheet.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }