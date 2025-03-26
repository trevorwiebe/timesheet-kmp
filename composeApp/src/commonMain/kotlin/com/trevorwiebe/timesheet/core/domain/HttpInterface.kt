package com.trevorwiebe.timesheet.core.domain

interface HttpInterface {

    suspend fun getHolidays(year: String, countryCode: String): TSResult

}