package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class HolidayDto(
    val date: String,
    val localName: String,
    val name: String,
    val countryCode: String,
    val fixed: Boolean,
    val global: Boolean,
    val counties: List<String>?,
    val launchYear: Int?,
    val types: List<String>,
)