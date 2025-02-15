package com.trevorwiebe.timesheet.core.model

import com.trevorwiebe.timesheet.core.domain.dto.OrganizationDto

data class Organization(
    val id: String,
    val name: String,
    val payPeriodDOWStart: String,
    val payPeriodDuration: Int,
    val payPeriodUnit: String
)

fun OrganizationDto.toOrganization(id: String): Organization {
    return Organization(
        id = id,
        name = name,
        payPeriodDOWStart = payPeriodDOWStart,
        payPeriodDuration = payPeriodDuration,
        payPeriodUnit = payperiodUnit
    )
}
