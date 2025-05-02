package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.Util.convertStringToLocalDateTime
import com.trevorwiebe.timesheet.core.domain.dto.PunchDto
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

data class Punch(
    val punchId: String,
    val dateTime: LocalDateTime,
    val rateId: String,
    val type: PunchType,
)

fun PunchDto.toPunch(punchId: String): Punch {
    return Punch(
        punchId = punchId,
        dateTime = convertStringToLocalDateTime(this.dateTime),
        rateId = this.rateId,
        type = PunchType.valueOf(this.type.uppercase())
    )
}

fun Punch.toPunchDto(): PunchDto {
    return PunchDto(
        dateTime = this.dateTime.toInstant(TimeZone.currentSystemDefault()).toString(),
        rateId = this.rateId,
        type = this.type.toString().lowercase()
    )
}

enum class PunchType {
    IN,
    OUT
}
