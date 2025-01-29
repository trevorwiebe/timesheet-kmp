package com.trevorwiebe.timesheet

import kotlinx.serialization.Serializable


sealed interface Route {

    @Serializable
    data object SignIn : Route
}