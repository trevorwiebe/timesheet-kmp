package com.trevorwiebe.timesheet

import androidx.compose.ui.window.ComposeUIViewController
import com.trevorwiebe.timesheet.di.platformModule
import com.trevorwiebe.timesheet.di.sharedModule
import org.koin.core.context.startKoin


fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin{
            modules(platformModule, sharedModule)
        }
    }
) {
    App()
}