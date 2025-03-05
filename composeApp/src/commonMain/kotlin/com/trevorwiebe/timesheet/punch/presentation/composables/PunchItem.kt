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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.Util.instantToFriendlyDate
import com.trevorwiebe.timesheet.core.domain.Util.instantToFriendlyDayOfWeek
import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.core.model.Rate
import com.trevorwiebe.timesheet.core.presentation.common.DestructiveButton
import com.trevorwiebe.timesheet.core.presentation.common.PunchPuckTime
import com.trevorwiebe.timesheet.core.presentation.common.RateSelector
import com.trevorwiebe.timesheet.core.presentation.common.TimeSheetButton
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import com.trevorwiebe.timesheet.theme.secondary
import kotlinx.datetime.Instant

@Composable
fun PunchItem(
    date: Instant,
    punches: List<UiPunch>,
    onShowConfirmDelete: (punchUiModel: UiPunch) -> Unit,
    onShowAddHours: () -> Unit,
    onUpdateRate: (UiPunch) -> Unit,
    onTimeSelected: (Punch) -> Unit,
    rateList: List<Rate>
) {
    var editing by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (editing) 4.dp else 0.dp
    )
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { editing = !editing }
            .padding(8.dp),
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            PunchHeader(date = date)
            PunchBody(
                editing = editing,
                punches = punches,
                onShowConfirmDelete = onShowConfirmDelete,
                onTimeSelected = onTimeSelected,
                rateList = rateList,
                onUpdateRate = onUpdateRate
            )
            ConfirmChangesRow(
                onShowAddHours = onShowAddHours,
                isEditing = editing
            )
        }
    }
}

@Composable
private fun PunchHeader(date: Instant) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = instantToFriendlyDayOfWeek(date),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = secondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = instantToFriendlyDate(date),
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
    rateList: List<Rate>
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (punches.isEmpty()) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "No punches for this day",
                fontSize = 14.sp,
                color = secondary
            )
        } else {
            punches.forEach { uiPunch ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("In: ", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            EditableTextField(uiPunch.punchIn, editing, onTimeSelected)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("Out: ", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            EditableTextField(uiPunch.punchOut, editing, onTimeSelected)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Rate:",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (editing) {
                                RateSelector(
                                    modifier = Modifier.width(100.dp),
                                    rateList = rateList,
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
                                    text = uiPunch.getRateName(rateList)
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = editing,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
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
        }
    }
}

@Composable
fun EditableTextField(
    punch: Punch?,
    isEditing: Boolean,
    onTimeSelected: (Punch) -> Unit
) {

    Box(
        modifier = Modifier.padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = isEditing, label = "EditableTextField") { editing ->
            if (editing && punch != null) {
                PunchPuckTime(
                    modifier = Modifier.width(100.dp),
                    initialTime = punch,
                    onTimeSelected = onTimeSelected,
                )
            } else {
                val time = remember(punch) { Util.instantToFriendlyTime(punch?.dateTime) }
                Text(
                    modifier = Modifier.width(80.dp).padding(0.dp),
                    text = time,
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeSheetButton(
                modifier = Modifier.width(150.dp).height(50.dp),
                onClick = onShowAddHours,
                text = "Add Hours"
            )
        }
    }
}