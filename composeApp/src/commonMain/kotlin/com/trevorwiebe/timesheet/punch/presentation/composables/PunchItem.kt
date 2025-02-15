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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetButton
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.secondary
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun PunchItem(date: Instant, punches: List<Triple<String, String, String>>) {
    var editing by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (editing) 4.dp else 0.dp
    )
    Card(
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
                punches = punches
            )
            ConfirmChangesRow(onConfirm = {
                editing = false
            }, onCancel = {
                editing = false
            }, isEditing = editing)
        }
    }
}

@Composable
private fun PunchHeader(date: Instant) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = date.toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfWeek.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = secondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = date.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = secondary
        )
    }
}

@Composable
private fun PunchBody(editing: Boolean, punches: List<Triple<String, String, String>>) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        punches.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("In: ", fontWeight = FontWeight.Bold)
                EditableTextField(it.first, editing, {})
                Spacer(modifier = Modifier.weight(1f))
                Text("Out: ", fontWeight = FontWeight.Bold)
                EditableTextField(it.second, editing, {})
                Spacer(modifier = Modifier.weight(1f))
                Text("Rate: ", fontWeight = FontWeight.Bold)
                Text(it.third)
            }
        }
    }
}

@Composable
fun EditableTextField(
    text: String,
    isEditing: Boolean,
    onTextChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier.padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = isEditing, label = "EditableTextField") { editing ->
            if (editing) {
                BasicTextField(
                    modifier = Modifier
                        .background(
                            color = primary,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp)
                        .width(65.dp),
                    value = text,
                    onValueChange = onTextChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
            } else {
                Text(
                    modifier = Modifier.width(80.dp).padding(0.dp),
                    text = text,
                )
            }
        }
    }
}

@Composable
fun ConfirmChangesRow(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
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
            TimesheetButton(
                modifier = Modifier.width(150.dp),
                text = "Add Hours",
                onClick = onConfirm,
                loading = false
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onCancel,
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color(230, 74, 25)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onConfirm,
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Confirm",
                    tint = Color(76, 175, 80)
                )
            }
        }
    }
}