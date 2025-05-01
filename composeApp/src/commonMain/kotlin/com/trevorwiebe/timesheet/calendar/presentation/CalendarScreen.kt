package com.trevorwiebe.timesheet.calendar.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.calendar.presentation.composables.DayBlock
import com.trevorwiebe.timesheet.calendar.presentation.composables.DayList
import com.trevorwiebe.timesheet.calendar.presentation.composables.DayOfWeekText
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.CalendarType
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.presentation.TopBar
import com.trevorwiebe.timesheet.core.presentation.common.NativeDialog
import com.trevorwiebe.timesheet.theme.errorRedText
import com.trevorwiebe.timesheet.theme.successGreenText
import kotlinx.datetime.isoDayNumber
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import timesheet.composeapp.generated.resources.Res
import timesheet.composeapp.generated.resources.baseline_calendar_view_day_24
import timesheet.composeapp.generated.resources.baseline_calendar_view_month_24
import timesheet.composeapp.generated.resources.cancel_circle
import timesheet.composeapp.generated.resources.check_circle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsState()

    val lazyColumnState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit, state.calendarType) {
        if (state.calendarType == CalendarType.GRID) {
            lazyGridState.scrollToItem(1005)
        } else {
            lazyColumnState.scrollToItem(1032)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = if (state.timeOffMode) "Adding Time Off" else "Time Off",
                actions = {
                    if (state.timeOffMode) {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(CalendarEvent.OnSetAddTimeOffMode(false, false))
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                painter = painterResource(Res.drawable.cancel_circle),
                                contentDescription = "cancel",
                                tint = errorRedText
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(
                            onClick = {
                                viewModel.onEvent(CalendarEvent.OnSetAddTimeOffMode(false, true))
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                painter = painterResource(Res.drawable.check_circle),
                                contentDescription = "check",
                                tint = successGreenText
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    } else {
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
                    items(state.calendarStructure) { dayUi ->
                        DayBlock(
                            dayUi = dayUi,
                            onClick = {
                                if (state.timeOffMode) {
                                    viewModel.onEvent(CalendarEvent.OnSetSelectedTimeOff(dayUi.date))
                                } else {
                                    viewModel.onEvent(CalendarEvent.OnSetCalendarType(CalendarType.LIST))
                                }
                            },
                            onLongClick = {
                                viewModel.onEvent(CalendarEvent.OnSetAddTimeOffMode(true, false))
                                viewModel.onEvent(CalendarEvent.OnSetSelectedTimeOff(dayUi.date))
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = lazyColumnState
                ) {
                    state.calendarStructure
                        .groupBy { it.date.year to it.date.month }
                        .forEach { (yearMonth, daysInMonth) ->

                            stickyHeader {
                                // Replace with your own composable or styling
                                Text(
                                    text = "${yearMonth.second.name} ${yearMonth.first}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(
                                            start = 16.dp,
                                            top = 8.dp,
                                            bottom = 8.dp,
                                            end = 16.dp
                                        ),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            items(daysInMonth) { dayUi ->
                                DayList(
                                    dayUi = dayUi,
                                    onClick = {
                                        if (state.timeOffMode) {
                                            viewModel.onEvent(
                                                CalendarEvent.OnSetSelectedTimeOff(
                                                    dayUi.date
                                                )
                                            )
                                        }
                                    },
                                    onLongClick = {
                                        viewModel.onEvent(
                                            CalendarEvent.OnSetAddTimeOffMode(
                                                true,
                                                false
                                            )
                                        )
                                        viewModel.onEvent(CalendarEvent.OnSetSelectedTimeOff(dayUi.date))
                                    },
                                    onEmployeeSelected = {
                                        if (it.employeeId == state.user?.uid) {
                                            viewModel.onEvent(CalendarEvent.OnTimeOffSelected(it))
                                        }
                                    }
                                )
                            }
                        }
                }
            }
        }

        NativeDialog(
            confirmText = "Delete",
            onConfirm = {
                viewModel.onEvent(CalendarEvent.OnDeleteTimeOffRequest(state.timeOffModel))
                viewModel.onEvent(CalendarEvent.OnTimeOffSelected(null))
            },
            dismissText = "Cancel",
            onDismiss = {
                viewModel.onEvent(CalendarEvent.OnTimeOffSelected(null))
            },
            visible = state.showTimeOffDialog,
            title = "Delete Time Off",
            message = "Request date: ${Util.toFriendlyDate(state.timeOffModel?.requestOffTime)}",
        )
    }
}