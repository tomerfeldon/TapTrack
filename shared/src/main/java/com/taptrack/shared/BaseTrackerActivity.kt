package com.taptrack.shared

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.taptrack.shared.databinding.ActivityTrackerBinding
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

    /** Timestamps of each tap; the running index is rendered dynamically. */
    private val entryTimes = mutableListOf<String>()

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private lateinit var binding: ActivityTrackerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Keep content clear of the status/navigation bars, preserving the
        // base 16dp content padding.
        val basePadding = (16 * resources.displayMetrics.density).toInt()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                basePadding + bars.left,
                basePadding + bars.top,
                basePadding + bars.right,
                basePadding + bars.bottom,
            )
            insets
        }

        title = screenTitle

        binding.addButton.text = "Add $unitLabel"
        binding.addButton.setOnClickListener { onAdd() }
        binding.undoButton.setOnClickListener { onUndo() }
        binding.resetButton.setOnClickListener { onReset() }

        applyAccent()
        refresh()
    }

    private fun applyAccent() {
        binding.headerText.text = "$emoji  $screenTitle"
        binding.logo.setColorFilter(accentColor)
        binding.countText.setTextColor(accentColor)
        binding.addButton.backgroundTintList = ColorStateList.valueOf(accentColor)
        binding.progressBar.setIndicatorColor(accentColor)
    }

    private fun onAdd() {
        count++
        entryTimes += LocalTime.now().format(timeFormatter)
        refresh()
    }

    private fun onUndo() {
        if (count > 0) {
            count--
            entryTimes.removeAt(entryTimes.lastIndex)
            refresh()
        }
    }

    private fun onReset() {
        count = 0
        entryTimes.clear()
        refresh()
    }

    private fun refresh() {
        binding.countText.text = count.toCompactString()

        binding.progressBar.max = dailyGoal
        binding.progressBar.progress = count.coerceAtMost(dailyGoal)
        binding.progressText.text = if (count >= dailyGoal) {
            "🎉 Goal reached! (${count.toCompactString()}/${dailyGoal.toCompactString()})"
        } else {
            "${count.toCompactString()} / ${dailyGoal.toCompactString()}"
        }

        // Numbering is derived from the current list, so it stays correct
        // after an undo instead of keeping stale baked-in indices.
        binding.logText.text = entryTimes
            .mapIndexed { index, time -> "$unitLabel #${index + 1}  —  $time" }
            .joinToString("\n")
    }
}
