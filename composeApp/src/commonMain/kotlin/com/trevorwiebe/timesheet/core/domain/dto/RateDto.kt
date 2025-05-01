package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class RateDto(
    val id: String,
    val description: String,
    val userFacing: Boolean,
)
