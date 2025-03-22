package com.trevorwiebe.timesheet.di

import com.trevorwiebe.timesheet.core.data.CoreRepositoryImpl
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import com.trevorwiebe.timesheet.more.presentation.MoreViewModel
import com.trevorwiebe.timesheet.punch.data.PunchRepositoryImpl
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import com.trevorwiebe.timesheet.punch.domain.usecases.CalculateTimeSheets
import com.trevorwiebe.timesheet.punch.domain.usecases.ProcessPunchesForUi
import com.trevorwiebe.timesheet.punch.presentation.PunchViewModel
import com.trevorwiebe.timesheet.report.data.ReportRepositoryImpl
import com.trevorwiebe.timesheet.report.domain.ReportRepository
import com.trevorwiebe.timesheet.report.presentation.ReportViewModel
import com.trevorwiebe.timesheet.signin.data.AuthImpl
import com.trevorwiebe.timesheet.signin.domain.Authenticator
import com.trevorwiebe.timesheet.signin.presentation.auth.SignInViewModel
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
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    single<Authenticator> { AuthImpl(get(), get()) }
    single<PunchRepository> { PunchRepositoryImpl(get(), get()) }
    single<ReportRepository> { ReportRepositoryImpl(get(), get()) }
    single<GetCurrentPayPeriodStartAndEnd> { GetCurrentPayPeriodStartAndEnd() }
    single<CalculateTimeSheets> { CalculateTimeSheets(get()) }
    single<ProcessPunchesForUi> { ProcessPunchesForUi() }
    viewModelOf(::SignInViewModel)
    viewModelOf(::PunchViewModel)
    viewModelOf(::ReportViewModel)
    viewModelOf(::MoreViewModel)
}