package com.trevorwiebe.timesheet.core.domain.dto

import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.model.DatabaseUserModel
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseUserDto(
    val adminAccess: Boolean,
    val email: String,
    val fullTime: Boolean,
    val hireDate: String,
    val name: String,
    val organizationId: String,
    val ptoBalance: Int,
)

fun DatabaseUserDto.toDatabaseUserModel(id: String): DatabaseUserModel {
    return DatabaseUserModel(
        id = id,
        adminAccess = this.adminAccess,
        email = this.email,
        fullTime = this.fullTime,
        hireDate = Util.convertStringToLocalDate(this.hireDate),
        name = this.name,
        organizationId = this.organizationId,
        ptoBalance = this.ptoBalance
    )
}
