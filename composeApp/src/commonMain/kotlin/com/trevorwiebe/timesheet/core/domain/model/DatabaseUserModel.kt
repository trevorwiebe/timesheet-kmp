package com.trevorwiebe.timesheet.core.domain.model

import kotlinx.datetime.LocalDate

data class DatabaseUserModel(
    val id: String,
    val adminAccess: Boolean,
    val email: String,
    val fullTime: Boolean,
    val hireDate: LocalDate,
    val name: String,
    val organizationId: String,
    val ptoBalance: Int,
)
