package com.trevorwiebe.timesheet.calendar.domain

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

actual fun vibrate() {
    val generator =
        UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
    generator.prepare()
    generator.impactOccurred()
}