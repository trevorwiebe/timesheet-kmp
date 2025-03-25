package com.trevorwiebe.timesheet

import kotlinx.serialization.Serializable


sealed interface Route {

    @Serializable
    data object SignIn : Route

    @Serializable
    data object Punch : Route

    @Serializable
    data object Report : Route

    @Serializable
    data class ReportDetail(
        val startTime: String,
        val endTime: String,
        val timeSheetId: String,
    ) : Route

    @Serializable
    data object Calendar :Route

    @Serializable
    data object More : Route

}