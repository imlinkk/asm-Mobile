package com.example.kotlinquiz.data

import android.content.Context
import kotlin.math.roundToInt

data class HighScore(
    val score: Int,
    val total: Int,
    val percent: Int,
    val durationMillis: Long
)

data class TopicHighScore(
    val topic: QuizTopic,
    val highScore: HighScore
)

class HighScoreStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun get(topic: QuizTopic): HighScore? {
        val scoreKey = key(topic, KEY_SCORE)
        if (!prefs.contains(scoreKey)) return null

        return HighScore(
            score = prefs.getInt(scoreKey, 0),
            total = prefs.getInt(key(topic, KEY_TOTAL), 10),
            percent = prefs.getInt(key(topic, KEY_PERCENT), 0),
            durationMillis = prefs.getLong(key(topic, KEY_DURATION), 0L)
        )
    }

    fun getBestOverall(): TopicHighScore? {
        return QuizTopic.values()
            .mapNotNull { topic -> get(topic)?.let { TopicHighScore(topic, it) } }
            .maxWithOrNull(
                compareBy<TopicHighScore> { it.highScore.percent }
                    .thenBy { it.highScore.score }
                    .thenByDescending { -it.highScore.durationMillis }
            )
    }

    fun saveIfBest(topic: QuizTopic, score: Int, total: Int, durationMillis: Long): Boolean {
        val percent = calculatePercent(score, total)
        val current = get(topic)
        val isBetter = current == null ||
            percent > current.percent ||
            (percent == current.percent && durationMillis < current.durationMillis)

        if (!isBetter) return false

        prefs.edit()
            .putInt(key(topic, KEY_SCORE), score)
            .putInt(key(topic, KEY_TOTAL), total)
            .putInt(key(topic, KEY_PERCENT), percent)
            .putLong(key(topic, KEY_DURATION), durationMillis)
            .apply()

        return true
    }

    private fun key(topic: QuizTopic, suffix: String): String = "${topic.id}_$suffix"

    private fun calculatePercent(score: Int, total: Int): Int {
        if (total <= 0) return 0
        return ((score.toFloat() / total.toFloat()) * 100f).roundToInt()
    }

    private companion object {
        const val PREFS_NAME = "quiz_high_scores"
        const val KEY_SCORE = "score"
        const val KEY_TOTAL = "total"
        const val KEY_PERCENT = "percent"
        const val KEY_DURATION = "duration"
    }
}
