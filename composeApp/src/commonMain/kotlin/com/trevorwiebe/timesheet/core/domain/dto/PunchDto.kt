package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class PunchDto(
    val dateTime: String,
    val rateId: String,
    val type: String,
)
