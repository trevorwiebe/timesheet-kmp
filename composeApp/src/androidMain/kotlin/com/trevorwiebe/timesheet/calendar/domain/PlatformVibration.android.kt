package com.trevorwiebe.timesheet.calendar.domain

import android.Manifest
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission

lateinit var appContext: Context

fun initVibration(context: Context) {
    appContext = context.applicationContext
}

@RequiresPermission(Manifest.permission.VIBRATE)
actual fun vibrate() {
    val vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
}