package com.example.kotlinquiz.ui.quiz

enum class FeedbackType {
    CORRECT,
    WRONG,
    TIMEOUT
}

data class QuizUiState(
    val topicTitle: String,
    val questionNumber: Int,
    val totalQuestions: Int,
    val questionText: String,
    val options: List<String>,
    val selectedIndex: Int?,
    val correctIndex: Int?,
    val isAnswered: Boolean,
    val isLastQuestion: Boolean,
    val score: Int,
    val secondsRemaining: Int,
    val feedbackType: FeedbackType?,
    val explanation: String?
)

data class QuizFinishedEvent(
    val topicId: String,
    val questionIds: IntArray,
    val selectedAnswers: IntArray,
    val timeouts: BooleanArray,
    val score: Int,
    val total: Int,
    val durationMillis: Long
)
