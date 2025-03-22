package com.trevorwiebe.timesheet.more.presentation.dialogsAndSheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.presentation.common.TimeSheetButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun ConfirmSignOut(
    show: Boolean,
    loadingSignOut: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    text = "Are you sure you want to sign out?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(16.dp))
                TimeSheetButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Yes",
                    onClick = onConfirm,
                    loading = loadingSignOut
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}