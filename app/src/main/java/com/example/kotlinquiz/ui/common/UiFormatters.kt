package com.example.kotlinquiz.ui.common

import java.util.Locale
import kotlin.math.roundToInt

object UiFormatters {
    fun percent(score: Int, total: Int): Int {
        if (total <= 0) return 0
        return ((score.toFloat() / total.toFloat()) * 100f).roundToInt()
    }

    fun duration(millis: Long): String {
        val totalSeconds = (millis / 1000L).coerceAtLeast(0L)
        val minutes = totalSeconds / 60L
        val seconds = totalSeconds % 60L
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}
