package com.trevorwiebe.timesheet.punch.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun PayPeriodInfo(
    show: Boolean,
    onDismiss: () -> Unit,
    currentPayPeriod: Pair<LocalDate, LocalDate>?,
    hoursMap: List<Pair<String, Double>>,
) {

    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Pay Period Info",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = tertiary
                )

                Spacer(modifier = Modifier.height(16.dp))

                val friendlyStartDate = remember { Util.toFriendlyDate(currentPayPeriod?.first) }
                val friendlyEndDate = remember { Util.toFriendlyDate(currentPayPeriod?.second) }
                Text(
                    text = "Current Period:",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 16.sp,
                )
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "$friendlyStartDate - $friendlyEndDate",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total Hours Worked:",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 16.sp,
                )
                hoursMap.forEach { (rate, hours) ->
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "$rate: $hours",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}