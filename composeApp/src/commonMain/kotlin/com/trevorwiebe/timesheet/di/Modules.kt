package com.trevorwiebe.timesheet.di

import com.trevorwiebe.timesheet.authentication.data.AuthImpl
import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import com.trevorwiebe.timesheet.authentication.presentation.auth.SignInViewModel
import com.trevorwiebe.timesheet.core.data.CoreRepositoryImpl
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.punch.data.PunchRepositoryImpl
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import com.trevorwiebe.timesheet.punch.domain.usecases.CalculateTimeSheets
import com.trevorwiebe.timesheet.punch.domain.usecases.ProcessPunchesForUi
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
    var isDebug: Boolean = true
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
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    single<Authenticator> { AuthImpl(get(), get()) }
    single<PunchRepository> { PunchRepositoryImpl(get(), get()) }
    single<CalculateTimeSheets> { CalculateTimeSheets() }
    single<ProcessPunchesForUi> { ProcessPunchesForUi() }
    viewModelOf(::SignInViewModel)
    viewModelOf(::PunchViewModel)
}