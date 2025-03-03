package com.trevorwiebe.timesheet

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.trevorwiebe.timesheet.core.presentation.NativeViewFactory
import com.trevorwiebe.timesheet.di.platformModule
import com.trevorwiebe.timesheet.di.sharedModule
import org.koin.core.context.startKoin


val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("LocalNativeViewFactory not provided")
}

fun MainViewController(
    nativeViewFactory: NativeViewFactory
) = ComposeUIViewController(
    configure = {
        startKoin{
            modules(platformModule, sharedModule)
        }
    }
) {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        App()
    }
}