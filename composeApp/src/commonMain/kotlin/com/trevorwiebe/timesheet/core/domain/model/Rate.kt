package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.RateDto

data class Rate(
    val id: String,
    val description: String,
    val userFacing: Boolean,
)

fun RateDto.toRate(): Rate {
    return Rate(
        id = id,
        description = description,
        userFacing = userFacing
    )
}