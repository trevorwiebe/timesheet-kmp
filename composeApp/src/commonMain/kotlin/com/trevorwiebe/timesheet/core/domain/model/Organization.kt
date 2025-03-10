package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.Util.convertStringToLocalDate
import com.trevorwiebe.timesheet.core.domain.dto.OrganizationDto
import kotlinx.datetime.LocalDate

data class Organization(
    val id: String,
    val name: String,
    val goLiveDate: LocalDate
)

fun OrganizationDto.toOrganization(id: String): Organization {
    return Organization(
        id = id,
        name = name,
        goLiveDate = convertStringToLocalDate(goLiveDate)
    )
}
