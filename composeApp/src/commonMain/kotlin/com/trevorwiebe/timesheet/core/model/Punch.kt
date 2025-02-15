package com.trevorwiebe.timesheet.core.model

import com.trevorwiebe.timesheet.core.domain.dto.PunchDto
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

data class Punch(
    val punchId: String,
    val dateTime: Instant,
    val rateId: String
)

fun PunchDto.toPunch(punchId: String): Punch {
    return Punch(
        punchId = punchId,
        dateTime = Instant.parse(this.dateTime),
        rateId = this.rateId
    )
}

fun Punch.toPunchDto(): PunchDto {
    return PunchDto(
        dateTime = this.dateTime.toString(),
        rateId = this.rateId
    )
}
