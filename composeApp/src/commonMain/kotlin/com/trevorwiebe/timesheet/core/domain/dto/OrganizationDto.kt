package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationDto(
    val name: String,
    val payPeriodDOWStart: String,
    val payPeriodDuration: Int,
    val payperiodUnit: String
)
