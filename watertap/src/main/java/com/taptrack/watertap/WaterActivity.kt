package com.taptrack.watertap

import com.taptrack.shared.BaseTrackerActivity

class WaterActivity : BaseTrackerActivity() {
    override val screenTitle = "Water Intake Tracker"
    override val unitLabel = "glass of water"
    override val dailyGoal = 8
    override val accentColor = 0xFF2196F3.toInt()
    override val emoji = "💧"
}
