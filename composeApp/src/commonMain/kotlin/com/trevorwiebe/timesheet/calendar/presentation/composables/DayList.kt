package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.theme.calendarBackground
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DayList(
    dayUi: DayUi,
) {

    val day = remember { Util.toFriendlyDayOfWeek(dayUi.date) }
    val date = remember { Util.toFriendlyDate(dayUi.date) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(calendarBackground)
            .padding(0.dp, 0.dp, 0.dp, 8.dp)
    ) {
        if (dayUi.date == Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        ) {
            TodayListDateLabel(day = day, date = date)
        } else {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp),
                    text = day,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp),
                    text = date,
                )
            }

        }

//        val filteredEventList = remember(dayUi.eventList){
//            dayUi.eventList.filter { it.summary != "" }
//        }
//
//        filteredEventList.forEach{
//            DayListEventItem(
//                eventSummary = it.summary,
//                eventColor = it.color,
//                eventStartDate = it.startDate.toFriendlyTime(),
//                eventEndDate = it.endDate.toFriendlyTime()
//            )
//        }
    }

}