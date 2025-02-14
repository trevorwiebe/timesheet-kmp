package com.trevorwiebe.timesheet.punch.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetButton
import com.trevorwiebe.timesheet.punch.presentation.composables.PunchItem
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun PunchScreen() {

    Scaffold(
        topBar = {
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Time Sheet",
                    color = tertiary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it).fillMaxSize()
        ) {
            item {
                PunchItem()
                PunchItem()
                PunchItem()
            }
            item {
                AddPunch()
            }
        }
    }
}

@Composable
private fun AddPunch() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        TimesheetButton(
            modifier = Modifier.width(150.dp),
            text = "In/Out",
            onClick = {},
            loading = false
        )
        Spacer(modifier = Modifier.weight(1f))
        TimesheetButton(
            modifier = Modifier.width(150.dp),
            text = "Add PTO",
            onClick = {},
            loading = false
        )
    }
}