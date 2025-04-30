package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.model.TimeOffRequestModel
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.successGreenBackground
import com.trevorwiebe.timesheet.theme.successGreenText

@Composable
fun DayListEventItem(
    timeOffRequestModel: TimeOffRequestModel,
    approved: Boolean,
    onClick: () -> Unit,
) {

    val backgroundColor = if (approved) successGreenBackground else primary
    val textColor = if (approved) successGreenText else Color.Black

    val approvedDate =
        remember { "Approved on: ${Util.toFriendlyDate(timeOffRequestModel.timeOffRequestApproveTime)}" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(8.dp)
    ) {
        Text(
            text = timeOffRequestModel.employeeName ?: "",
            fontSize = 16.sp,
            modifier = Modifier,
            maxLines = 1,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        if (approved) {
            Text(
                text = approvedDate,
                fontSize = 14.sp,
                modifier = Modifier,
                maxLines = 1,
                color = textColor,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}