package com.trevorwiebe.timesheet.di

import com.trevorwiebe.timesheet.authentication.data.AuthImpl
import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import com.trevorwiebe.timesheet.authentication.presentation.auth.SignInViewModel
import com.trevorwiebe.timesheet.punch.data.PuncherImpl
import com.trevorwiebe.timesheet.punch.domain.Puncher
import com.trevorwiebe.timesheet.punch.presentation.PunchViewModel
import dev.gitlive.firebase.Firebase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

interface FirebaseEmulatorConfig {
    fun setupEmulators(firebase: Firebase)
}

expect fun createEmulatorConfig(): FirebaseEmulatorConfig

object Debug {
    // This will be set during initialization
    var isDebug: Boolean = false
}

expect val platformModule: Module

val sharedModule = module {
    single<Firebase> {
        val firebase = Firebase
        if (Debug.isDebug) {
            val emulatorConfig = createEmulatorConfig()
            emulatorConfig.setupEmulators(firebase)
        }
        firebase
    }
    single<Authenticator> { AuthImpl(get()) }
    single<Puncher> { PuncherImpl(get()) }
    viewModelOf(::SignInViewModel)
    viewModelOf(::PunchViewModel)
}