package com.trevorwiebe.timesheet.calendar.presentation

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.trevorwiebe.timesheet.core.presentation.TopBar

@Composable
fun CalendarScreen() {


    Scaffold(
        topBar = { TopBar("Time Off") }
    ) {

    }
}