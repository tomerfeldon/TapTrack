package com.taptrack.shared

import java.util.Locale

/**
 * Formats a number compactly:
 *   1_000          -> "1k"
 *   1_500          -> "1.5k"
 *   2_000_000      -> "2m"
 *   3_000_000_000  -> "3b"
 * A trailing ".0" is trimmed, and formatting always uses [Locale.US].
 */
fun Number.toCompactString(): String {
    val value = this.toDouble()
    return when {
        value >= 1_000_000_000 -> formatCompact(value / 1_000_000_000, "b")
        value >= 1_000_000 -> formatCompact(value / 1_000_000, "m")
        value >= 1_000 -> formatCompact(value / 1_000, "k")
        else -> formatWithoutTrailingZero(value)
    }
}

private fun formatCompact(value: Double, suffix: String): String =
    formatWithoutTrailingZero(value) + suffix

private fun formatWithoutTrailingZero(value: Double): String =
    String.format(Locale.US, "%.1f", value).removeSuffix(".0")
