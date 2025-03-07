package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.OrganizationDto
import kotlinx.datetime.Instant

data class Organization(
    val id: String,
    val name: String,
    val goLiveDate: Instant
)

fun OrganizationDto.toOrganization(id: String): Organization {
    return Organization(
        id = id,
        name = name,
        goLiveDate = Instant.parse(goLiveDate)
    )
}
