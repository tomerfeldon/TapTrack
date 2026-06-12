package com.taptrack.stretchtap

import com.taptrack.shared.BaseTrackerActivity

class StretchActivity : BaseTrackerActivity() {
    override val screenTitle = "Stretching Tracker"
    override val unitLabel = "stretch set"
    override val dailyGoal = 5
    override val accentColor = 0xFF4CAF50.toInt()
    override val emoji = "🧘"
}
