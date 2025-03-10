package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.PunchDto
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class Punch(
    val punchId: String,
    val dateTime: LocalDateTime,
    val rateId: String
)

fun PunchDto.toPunch(punchId: String): Punch {
    return Punch(
        punchId = punchId,
        dateTime = Instant.parse(this.dateTime).toLocalDateTime(TimeZone.currentSystemDefault()),
        rateId = this.rateId
    )
}

fun Punch.toPunchDto(): PunchDto {
    return PunchDto(
        dateTime = this.dateTime.toInstant(TimeZone.currentSystemDefault()).toString(),
        rateId = this.rateId
    )
}
