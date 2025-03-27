package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.HolidayDto
import kotlinx.datetime.LocalDate

data class Holiday(
    val date: LocalDate,
    val localName: String,
    val name: String,
    val countryCode: String,
    val fixed: Boolean,
    val global: Boolean,
    val counties: List<String>?,
    val launchYear: Int?,
    val types: List<String>,
)

fun HolidayDto.toHoliday(): Holiday {
    return Holiday(
        date = LocalDate.parse(date),
        localName = localName,
        name = name,
        countryCode = countryCode,
        fixed = fixed,
        global = global,
        counties = counties,
        launchYear = launchYear,
        types = types
    )
}