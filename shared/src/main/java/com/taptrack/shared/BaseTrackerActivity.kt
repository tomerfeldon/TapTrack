package com.taptrack.shared

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Template Method base for every tracker screen.
 *
 * All of the UI wiring, counting, and logging lives here. A concrete app only
 * provides the two pieces that actually differ between trackers: the screen
 * title and the unit being counted. Subclasses should add no behavior of their
 * own — that is the whole point of the assignment.
 */
abstract class BaseTrackerActivity : AppCompatActivity() {

    /** Title shown in the action bar, e.g. "Water Intake Tracker". */
    abstract val screenTitle: String

    /** Singular unit being counted, e.g. "glass of water". */
    abstract val unitLabel: String

    private var count = 0
    private val entries = mutableListOf<String>()

    private lateinit var countText: TextView
    private lateinit var logText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)

        title = screenTitle

        countText = findViewById(R.id.countText)
        logText = findViewById(R.id.logText)

        val addButton: Button = findViewById(R.id.addButton)
        addButton.text = "Add $unitLabel"
        addButton.setOnClickListener { onAdd() }

        refresh()
    }

    private fun onAdd() {
        count++
        entries += "$unitLabel #$count"
        refresh()
    }

    private fun refresh() {
        countText.text = count.toString()
        logText.text = entries.joinToString("\n")
    }
}
