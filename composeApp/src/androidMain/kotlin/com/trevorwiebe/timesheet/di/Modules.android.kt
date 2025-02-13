package com.trevorwiebe.timesheet.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single { Firebase.initialize(androidContext())}
}