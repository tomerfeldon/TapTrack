package com.taptrack.shared

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Template Method base for every tracker screen.
 *
 * All of the UI wiring, counting, logging, progress and theming lives here. A
 * concrete app only provides the handful of values that actually differ between
 * trackers — title, unit, daily goal, accent color and emoji. Subclasses should
 * add no behavior of their own; that is the whole point of the assignment.
 */
abstract class BaseTrackerActivity : AppCompatActivity() {

    /** Title shown in the action bar and header, e.g. "Water Intake Tracker". */
    abstract val screenTitle: String

    /** Singular unit being counted, e.g. "glass of water". */
    abstract val unitLabel: String

    /** Target used by the progress bar, e.g. 8 glasses a day. */
    abstract val dailyGoal: Int

    /** Accent color applied to the number, button and progress bar. */
    @get:ColorInt
    abstract val accentColor: Int

    /** Emoji shown in the header next to the title, e.g. "💧". */
    abstract val emoji: String

    private var count = 0
    private val entries = mutableListOf<String>()

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private lateinit var headerText: TextView
    private lateinit var countText: TextView
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var progressText: TextView
    private lateinit var logText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)

        title = screenTitle

        headerText = findViewById(R.id.headerText)
        countText = findViewById(R.id.countText)
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)
        logText = findViewById(R.id.logText)

        val addButton: MaterialButton = findViewById(R.id.addButton)
        val undoButton: MaterialButton = findViewById(R.id.undoButton)
        val resetButton: MaterialButton = findViewById(R.id.resetButton)
        addButton.text = "Add $unitLabel"
        addButton.setOnClickListener { onAdd() }
        undoButton.setOnClickListener { onUndo() }
        resetButton.setOnClickListener { onReset() }

        applyAccent(addButton)
        refresh()
    }

    private fun applyAccent(addButton: MaterialButton) {
        headerText.text = "$emoji  $screenTitle"
        countText.setTextColor(accentColor)
        addButton.backgroundTintList = ColorStateList.valueOf(accentColor)
        progressBar.setIndicatorColor(accentColor)
    }

    private fun onAdd() {
        count++
        val time = LocalTime.now().format(timeFormatter)
        entries += "$unitLabel #$count  —  $time"
        refresh()
    }

    private fun onUndo() {
        if (count > 0) {
            count--
            entries.removeAt(entries.lastIndex)
            refresh()
        }
    }

    private fun onReset() {
        count = 0
        entries.clear()
        refresh()
    }

    private fun refresh() {
        countText.text = count.toString()

        progressBar.max = dailyGoal
        progressBar.progress = count.coerceAtMost(dailyGoal)
        progressText.text = if (count >= dailyGoal) {
            "🎉 Goal reached! ($count/$dailyGoal)"
        } else {
            "$count / $dailyGoal"
        }

        logText.text = entries.joinToString("\n")
    }
}
