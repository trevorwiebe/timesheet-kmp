package com.trevorwiebe.timesheet

import android.app.Application
import com.trevorwiebe.timesheet.di.initKoin
import org.koin.android.ext.koin.androidContext

class TimeSheetApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TimeSheetApplication)
        }
    }
}