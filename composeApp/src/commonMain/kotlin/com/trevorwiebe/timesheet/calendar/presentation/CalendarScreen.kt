package com.trevorwiebe.timesheet.calendar.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.calendar.presentation.composables.DayBlock
import com.trevorwiebe.timesheet.calendar.presentation.composables.DayList
import com.trevorwiebe.timesheet.calendar.presentation.composables.DayOfWeekText
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.CalendarType
import com.trevorwiebe.timesheet.core.presentation.TopBar
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import timesheet.composeapp.generated.resources.Res
import timesheet.composeapp.generated.resources.baseline_calendar_view_day_24
import timesheet.composeapp.generated.resources.baseline_calendar_view_month_24

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsState()

    val currentDate =
        Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).date

    val lazyGridState = rememberLazyGridState()
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(state.calendarStructure, lazyGridState, lazyColumnState) {
        println(currentDate)
        val scrollToPosition = state.calendarStructure.indexOfFirst {
            it.date == currentDate
        }
        if (scrollToPosition != -1) {
            lazyColumnState.scrollToItem(100)
            lazyGridState.scrollToItem(100)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Time Off",
                actions = {
                    if (state.calendarType == CalendarType.GRID) {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(CalendarEvent.OnSetCalendarType(CalendarType.LIST))
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.baseline_calendar_view_day_24),
                                contentDescription = "calendar day view"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(CalendarEvent.OnSetCalendarType(CalendarType.GRID))
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.baseline_calendar_view_month_24),
                                contentDescription = "calendar month view"
                            )
                        }
                    }
                }
            )
        }
    ) {

        Column {

            if (state.calendarType == CalendarType.GRID) {

                Row {
                    DayOfWeekText(dayInitial = "S", modifier = Modifier.weight(1f))
                    DayOfWeekText(dayInitial = "M", modifier = Modifier.weight(1f))
                    DayOfWeekText(dayInitial = "T", modifier = Modifier.weight(1f))
                    DayOfWeekText(dayInitial = "W", modifier = Modifier.weight(1f))
                    DayOfWeekText(dayInitial = "T", modifier = Modifier.weight(1f))
                    DayOfWeekText(dayInitial = "F", modifier = Modifier.weight(1f))
                    DayOfWeekText(dayInitial = "S", modifier = Modifier.weight(1f))
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                val firstDate = state.calendarStructure.first().date.dayOfWeek.isoDayNumber

                LazyVerticalGrid(
                    state = lazyGridState,
                    columns = GridCells.Fixed(7)
                ) {
                    for (i in 1..firstDate) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .height(96.dp)
                            )
                        }
                    }
                    items(state.calendarStructure) {
                        DayBlock(dayUi = it)
                    }
                }
            } else {
                LazyColumn(
                    state = lazyColumnState
                ) {
                    items(state.calendarStructure) {
                        DayList(dayUi = it)
                    }
                }
            }
        }
    }
}