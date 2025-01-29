package com.trevorwiebe.timesheet.di

import org.koin.core.module.dsl.viewModelOf
import com.trevorwiebe.timesheet.authentication.presentation.auth.SignInViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    viewModelOf(::SignInViewModel)
}