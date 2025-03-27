package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi
import com.trevorwiebe.timesheet.theme.calendarBackground
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DayBlock(
    dayUi: DayUi,
) {

    Column(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(calendarBackground)
            .height(96.dp)
    ) {
        if (dayUi.date == Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        ) {
            TodayBlockDateLabel(date = dayUi.date.dayOfMonth.toString())
        } else {
            Text(
                modifier = Modifier.padding(2.dp, 0.dp, 2.dp, 0.dp),
                text = dayUi.date.dayOfMonth.toString(),
                fontSize = 14.sp,
//                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

//        val filteredEventList = remember(dayUi.eventList){
//            dayUi.eventList.filter { it.summary != "" }
//        }

//        if(filteredEventList.size <= 4) {
//            filteredEventList.forEach {
//                DayBlockEventItem(
//                    eventSummary = it.summary,
//                    eventColor = it.color
//                )
//            }
//        }else{
//            filteredEventList.take(4).forEach{
//                DayBlockEventItem(
//                    eventSummary = it.summary,
//                    eventColor = it.color
//                )
//            }
//            MoreEventsEllipsis()
//        }
//    }
    }
}