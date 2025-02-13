package com.trevorwiebe.timesheet.punch.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Column(
            modifier = Modifier.padding(it).fillMaxSize()
        ) {

        }
    }
}