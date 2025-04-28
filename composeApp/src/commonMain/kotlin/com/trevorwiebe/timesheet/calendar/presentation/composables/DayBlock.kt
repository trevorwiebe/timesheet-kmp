package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi
import com.trevorwiebe.timesheet.theme.calendarBackground
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayBlock(
    dayUi: DayUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {

    val background = if (dayUi.selectedForTimeOff) tertiary else calendarBackground
    val todayLabelBackground = if (dayUi.selectedForTimeOff) Color.White else tertiary
    val todayLabelTextColor = if (dayUi.selectedForTimeOff) Color.Black else Color.White
    val otherDateTextColor = if (dayUi.selectedForTimeOff) Color.White else Color.Black

    Column(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(6.dp))
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .background(background)
            .height(96.dp)
    ) {
        if (dayUi.date == Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        ) {
            TodayBlockDateLabel(
                date = dayUi.date.dayOfMonth.toString(),
                backgroundColor = todayLabelBackground,
                textColor = todayLabelTextColor
            )
        } else {
            Text(
                modifier = Modifier.padding(2.dp, 0.dp, 2.dp, 0.dp),
                text = dayUi.date.dayOfMonth.toString(),
                fontSize = 14.sp,
                color = otherDateTextColor
            )
        }

        val userList = dayUi.employeesOff

        if (userList.size <= 3) {
            userList.forEach {
                DayBlockEventItem(
                    employeeName = it.employeeName ?: "unavailable",
                    approved = it.timeOffRequestApproveTime != null
                )
            }
        } else {
            userList.take(2).forEach {
                DayBlockEventItem(
                    employeeName = it.employeeName ?: "unavailable",
                    approved = it.timeOffRequestApproveTime != null
                )
            }
            MoreEventsEllipsis()
        }
    }
}