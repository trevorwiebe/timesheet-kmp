package com.trevorwiebe.timesheet.di

import com.trevorwiebe.timesheet.authentication.data.AuthImpl
import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import org.koin.core.module.dsl.viewModelOf
import com.trevorwiebe.timesheet.authentication.presentation.auth.SignInViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<FirebaseAuth> { Firebase.auth }
    single<Authenticator> { AuthImpl(get()) }
    viewModelOf(::SignInViewModel)
}