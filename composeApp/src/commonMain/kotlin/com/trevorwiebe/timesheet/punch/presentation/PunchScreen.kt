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
import androidx.compose.ui.unit.dp

@Composable
fun PunchScreen(
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Punch"
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