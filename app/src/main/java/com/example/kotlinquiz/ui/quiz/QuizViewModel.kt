package com.example.kotlinquiz.ui.quiz

import android.os.CountDownTimer
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinquiz.data.QuizRepository
import com.example.kotlinquiz.data.QuizTopic

class QuizViewModel(topicId: String) : ViewModel() {
    private val topic = QuizTopic.fromId(topicId)
    private val questions = QuizRepository.getQuizQuestions(topic.id, randomize = true)
    private val selectedAnswers = IntArray(questions.size) { NO_ANSWER }
    private val timeouts = BooleanArray(questions.size)
    private val quizStartedAt = SystemClock.elapsedRealtime()

    private val _uiState = MutableLiveData<QuizUiState>()
    val uiState: LiveData<QuizUiState> = _uiState

    private val _finishEvent = MutableLiveData<QuizFinishedEvent?>()
    val finishEvent: LiveData<QuizFinishedEvent?> = _finishEvent

    private var currentIndex = 0
    private var score = 0
    private var remainingMillis = QUESTION_TIME_MILLIS
    private var timer: CountDownTimer? = null
    private var finished = false

    init {
        if (questions.isEmpty()) {
            finishQuiz()
        } else {
            publishState()
            startTimer()
        }
    }

    fun selectAnswer(optionIndex: Int) {
        if (finished || questions.isEmpty() || isCurrentQuestionAnswered()) return
        val question = questions[currentIndex]
        if (optionIndex !in question.options.indices) return

        selectedAnswers[currentIndex] = optionIndex
        if (optionIndex == question.correctIndex) {
            score += 1
        }

        timer?.cancel()
        publishState()
    }

    fun nextQuestion() {
        if (finished || questions.isEmpty() || !isCurrentQuestionAnswered()) return

        if (currentIndex == questions.lastIndex) {
            finishQuiz()
            return
        }

        currentIndex += 1
        remainingMillis = QUESTION_TIME_MILLIS
        publishState()
        startTimer()
    }

    fun onFinishHandled() {
        _finishEvent.value = null
    }

    override fun onCleared() {
        timer?.cancel()
        super.onCleared()
    }

    private fun startTimer() {
        if (questions.isEmpty()) return
        timer?.cancel()
        timer = object : CountDownTimer(QUESTION_TIME_MILLIS, TIMER_TICK_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis = millisUntilFinished
                publishState()
            }

            override fun onFinish() {
                remainingMillis = 0L
                markTimeout()
            }
        }.start()
    }

    private fun markTimeout() {
        if (finished || questions.isEmpty() || isCurrentQuestionAnswered()) return
        timeouts[currentIndex] = true
        publishState()
    }

    private fun isCurrentQuestionAnswered(): Boolean {
        return selectedAnswers[currentIndex] != NO_ANSWER || timeouts[currentIndex]
    }

    private fun finishQuiz() {
        if (finished) return
        finished = true
        timer?.cancel()
        _finishEvent.value = QuizFinishedEvent(
            topicId = topic.id,
            questionIds = questions.map { it.id }.toIntArray(),
            selectedAnswers = selectedAnswers.copyOf(),
            timeouts = timeouts.copyOf(),
            score = score,
            total = questions.size,
            durationMillis = SystemClock.elapsedRealtime() - quizStartedAt
        )
    }

    private fun publishState() {
        if (questions.isEmpty()) return

        val question = questions[currentIndex]
        val selectedIndex = selectedAnswers[currentIndex].takeIf { it != NO_ANSWER }
        val timedOut = timeouts[currentIndex]
        val isAnswered = selectedIndex != null || timedOut
        val feedbackType = when {
            !isAnswered -> null
            timedOut -> FeedbackType.TIMEOUT
            selectedIndex == question.correctIndex -> FeedbackType.CORRECT
            else -> FeedbackType.WRONG
        }

        _uiState.value = QuizUiState(
            topicTitle = topic.title,
            questionNumber = currentIndex + 1,
            totalQuestions = questions.size,
            questionText = question.question,
            options = question.options,
            selectedIndex = selectedIndex,
            correctIndex = if (isAnswered) question.correctIndex else null,
            isAnswered = isAnswered,
            isLastQuestion = currentIndex == questions.lastIndex,
            score = score,
            secondsRemaining = ((remainingMillis + 999L) / 1000L).toInt().coerceIn(0, 30),
            feedbackType = feedbackType,
            explanation = if (isAnswered) question.explanation else null
        )
    }

    class Factory(private val topicId: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
                return QuizViewModel(topicId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    private companion object {
        const val QUESTION_TIME_MILLIS = 30_000L
        const val TIMER_TICK_MILLIS = 1_000L
        const val NO_ANSWER = -1
    }
}
