package com.trevorwiebe.timesheet.punch.presentation.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.Util.toFriendlyDate
import com.trevorwiebe.timesheet.core.domain.Util.toFriendlyDayOfWeek
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.core.presentation.common.DestructiveButton
import com.trevorwiebe.timesheet.core.presentation.common.RateSelector
import com.trevorwiebe.timesheet.core.presentation.common.TimeSheetButton
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.secondary
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.LocalDate

@Composable
fun PunchItem(
    editable: Boolean,
    date: LocalDate,
    holiday: String?,
    hoursWorked: List<Pair<String, Double>>,
    punches: List<UiPunch>,
    onShowConfirmDelete: (punchUiModel: UiPunch) -> Unit,
    onShowAddHours: () -> Unit,
    onUpdateRate: (UiPunch) -> Unit,
    onTimeSelected: (Punch) -> Unit,
    rateList: List<Rate>,
) {
    var editing by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (editing) 4.dp else 0.dp
    )
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = if (editable) {
            Modifier
                .fillMaxWidth()
                .clickable { editing = !editing }
                .padding(8.dp)
        } else {
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        },
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            PunchHeader(date = date, holiday = holiday)
            PunchBody(
                editing = editing,
                punches = punches,
                onShowConfirmDelete = onShowConfirmDelete,
                onTimeSelected = onTimeSelected,
                rateList = rateList,
                onUpdateRate = onUpdateRate,
                hoursWorked = hoursWorked
            )
            ConfirmChangesRow(
                onShowAddHours = onShowAddHours,
                isEditing = editing
            )
        }
    }
}

@Composable
private fun PunchHeader(date: LocalDate, holiday: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = toFriendlyDayOfWeek(date),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = secondary
        )
        if (holiday != null) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = holiday.toUpperCase(Locale.current),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = tertiary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = toFriendlyDate(date),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = secondary
        )
    }
}

@Composable
private fun PunchBody(
    editing: Boolean,
    punches: List<UiPunch>,
    onShowConfirmDelete: (punchUiModel: UiPunch) -> Unit,
    onTimeSelected: (Punch) -> Unit,
    onUpdateRate: (UiPunch) -> Unit,
    rateList: List<Rate>,
    hoursWorked: List<Pair<String, Double>>
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (punches.isEmpty()) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "No clock times for this day",
                fontSize = 14.sp,
                color = secondary
            )
        } else {
            punches.forEach { uiPunch ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                        ) {
                            Text("In: ", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            StyleTransitionTextField(uiPunch.punchIn, editing, onTimeSelected)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                        ) {
                            Text("Out: ", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            StyleTransitionTextField(uiPunch.punchOut, editing, onTimeSelected)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                        ) {
                            Text(
                                text = "Rate:",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (editing) {
                                RateSelector(
                                    modifier = Modifier.width(110.dp),
                                    rateList = rateList.filter { it.userFacing },
                                    selectedRate = uiPunch.getRate(rateList),
                                    onRateSelected = {
                                        val newUiPunch = uiPunch.copy(
                                            punchIn = uiPunch.punchIn.copy(rateId = it.id),
                                            punchOut = uiPunch.punchOut?.copy(rateId = it.id)
                                        )
                                        onUpdateRate(newUiPunch)
                                    }
                                )
                            } else {
                                Text(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .padding(
                                            start = 8.dp,
                                            end = 8.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        ),
                                    text = uiPunch.getRate(rateList)?.description ?: "unavailable"
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = editing,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Spacer(modifier = Modifier.height(6.dp))
                                DestructiveButton(
                                    modifier = Modifier.width(150.dp).height(50.dp),
                                    text = "Delete Time",
                                    onClick = { onShowConfirmDelete(uiPunch) }
                                )
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                hoursWorked.forEach {
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = "${it.first}: ${it.second}",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun StyleTransitionTextField(
    punch: Punch?,
    isEditing: Boolean,
    onTimeSelected: (Punch) -> Unit,
) {

    Box(
        modifier = Modifier.padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = isEditing, label = "EditableTextField") { editing ->

            val time = remember(punch) { Util.toFriendlyTime(punch?.dateTime) }

            if (editing && punch != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(primary)
                        .clickable { onTimeSelected(punch) }
                ) {
                    Text(
                        modifier = Modifier
                            .width(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                        text = time,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    modifier = Modifier.width(110.dp).padding(0.dp),
                    text = time,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ConfirmChangesRow(
    onShowAddHours: () -> Unit,
    isEditing: Boolean
) {
    AnimatedVisibility(
        visible = isEditing,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            TimeSheetButton(
                modifier = Modifier.width(150.dp).height(50.dp),
                onClick = onShowAddHours,
                text = "Add Hours",
                loading = false
            )
        }
    }
}