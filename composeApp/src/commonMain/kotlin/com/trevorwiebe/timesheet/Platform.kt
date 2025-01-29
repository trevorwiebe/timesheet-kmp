package com.trevorwiebe.timesheet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform