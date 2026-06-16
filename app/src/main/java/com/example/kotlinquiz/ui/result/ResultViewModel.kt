package com.example.kotlinquiz.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinquiz.data.HighScoreStore
import com.example.kotlinquiz.data.QuizRepository
import com.example.kotlinquiz.data.QuizTopic
import com.example.kotlinquiz.ui.common.UiFormatters

data class ResultParams(
    val topicId: String,
    val questionIds: IntArray,
    val selectedAnswers: IntArray,
    val timeouts: BooleanArray,
    val score: Int,
    val total: Int,
    val durationMillis: Long
)

data class ReviewUiItem(
    val questionId: Int,
    val questionNumber: Int,
    val question: String,
    val selectedAnswer: String?,
    val correctAnswer: String,
    val explanation: String,
    val isCorrect: Boolean,
    val timedOut: Boolean
)

data class ResultUiState(
    val topicId: String,
    val title: String,
    val scoreText: String,
    val percent: Int,
    val durationText: String,
    val highScoreText: String,
    val isNewHighScore: Boolean,
    val reviewItems: List<ReviewUiItem>
)

class ResultViewModel(
    application: Application,
    params: ResultParams
) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData<ResultUiState>()
    val uiState: LiveData<ResultUiState> = _uiState

    init {
        _uiState.value = buildState(application, params)
    }

    private fun buildState(application: Application, params: ResultParams): ResultUiState {
        val topic = QuizTopic.fromId(params.topicId)
        val highScoreStore = HighScoreStore(application)
        val isNewHighScore = highScoreStore.saveIfBest(
            topic = topic,
            score = params.score,
            total = params.total,
            durationMillis = params.durationMillis
        )
        val savedHighScore = highScoreStore.get(topic)
        val questions = QuizRepository.getQuestionsByIds(params.questionIds)
        val reviewItems = questions.mapIndexed { index, question ->
            val selectedIndex = params.selectedAnswers.getOrElse(index) { NO_ANSWER }
            val timedOut = params.timeouts.getOrElse(index) { false }
            val selectedAnswer = question.options.getOrNull(selectedIndex)
            ReviewUiItem(
                questionId = question.id,
                questionNumber = index + 1,
                question = question.question,
                selectedAnswer = selectedAnswer,
                correctAnswer = question.options[question.correctIndex],
                explanation = question.explanation,
                isCorrect = selectedIndex == question.correctIndex,
                timedOut = timedOut
            )
        }

        val percent = UiFormatters.percent(params.score, params.total)
        val highScoreText = if (isNewHighScore) {
            "Điểm cao mới!"
        } else {
            savedHighScore?.let {
                "Điểm cao nhất: ${it.score}/${it.total} - ${it.percent}%"
            } ?: "Điểm cao nhất: chưa có"
        }

        return ResultUiState(
            topicId = topic.id,
            title = "Kết quả - ${topic.title}",
            scoreText = "${params.score}/${params.total}",
            percent = percent,
            durationText = UiFormatters.duration(params.durationMillis),
            highScoreText = highScoreText,
            isNewHighScore = isNewHighScore,
            reviewItems = reviewItems
        )
    }

    class Factory(
        private val application: Application,
        private val params: ResultParams
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
                return ResultViewModel(application, params) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    private companion object {
        const val NO_ANSWER = -1
    }
}
