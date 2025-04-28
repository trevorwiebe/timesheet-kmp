package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.theme.calendarBackground
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayList(
    dayUi: DayUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {

    val day = remember { Util.toFriendlyDayOfWeek(dayUi.date) }
    val date = remember { Util.toFriendlyDate(dayUi.date) }

    val background = if (dayUi.selectedForTimeOff) tertiary else calendarBackground
    val todayLabelBackground = if (dayUi.selectedForTimeOff) Color.White else tertiary
    val todayLabelTextColor = if (dayUi.selectedForTimeOff) Color.Black else Color.White
    val otherDateTextColor = if (dayUi.selectedForTimeOff) Color.White else Color.Black

    Column(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .fillMaxWidth()
            .background(background)
            .padding(4.dp, 4.dp, 4.dp, 8.dp)
    ) {
        if (dayUi.date == Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        ) {
            TodayListDateLabel(
                day = day,
                date = date,
                backgroundColor = todayLabelBackground,
                textColor = todayLabelTextColor
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp),
                    text = day,
                    color = otherDateTextColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp),
                    text = date,
                    color = otherDateTextColor
                )
            }
        }

        val userList = dayUi.employeesOff
        if (userList.size <= 3) {
            userList.forEach {
                DayListEventItem(
                    employeeName = it.employeeName ?: "unavailable",
                    approved = it.timeOffRequestApproveTime != null
                )
            }
        } else {
            userList.take(2).forEach {
                DayListEventItem(
                    employeeName = it.employeeName ?: "unavailable",
                    approved = it.timeOffRequestApproveTime != null
                )
            }
            MoreEventsEllipsis()
        }

    }

}