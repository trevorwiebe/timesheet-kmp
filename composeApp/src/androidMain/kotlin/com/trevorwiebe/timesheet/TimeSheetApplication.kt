package com.trevorwiebe.timesheet

import android.app.Application
import com.trevorwiebe.timesheet.di.platformModule
import com.trevorwiebe.timesheet.di.sharedModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.core.context.startKoin

class TimeSheetApplication: Application(){

    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)

        startKoin{
            modules(platformModule, sharedModule)
        }
    }
}