package com.trevorwiebe.timesheet.core.model

import com.trevorwiebe.timesheet.core.domain.dto.PunchDto
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Punch(
    val punchId: String,
    val dateTime: Instant,
    val rateId: String
)

fun PunchDto.toPunch(punchId: String): Punch {
    return Punch(
        punchId = punchId,
        dateTime = Clock.System.now(),
        rateId = this.rateId
    )
}

fun Punch.toPunchDto(): PunchDto {
    return PunchDto(
        dateTime = this.dateTime.toString(),
        rateId = this.rateId
    )
}
