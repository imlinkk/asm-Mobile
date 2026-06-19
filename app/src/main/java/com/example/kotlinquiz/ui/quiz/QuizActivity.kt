package com.example.kotlinquiz.ui.quiz

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kotlinquiz.R
import com.example.kotlinquiz.data.QuizTopic
import com.example.kotlinquiz.ui.common.WindowInsetsHelper
import com.example.kotlinquiz.ui.result.ResultActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator

class QuizActivity : AppCompatActivity() {
    private val viewModel: QuizViewModel by viewModels {
        QuizViewModel.Factory(intent.getStringExtra(EXTRA_TOPIC_ID) ?: QuizTopic.KOTLIN_BASICS.id)
    }

    private lateinit var optionButtons: List<MaterialButton>
    private lateinit var quizTopicTitle: TextView
    private lateinit var questionCounter: TextView
    private lateinit var scoreCounter: TextView
    private lateinit var questionProgress: LinearProgressIndicator
    private lateinit var timerText: TextView
    private lateinit var questionText: TextView
    private lateinit var feedbackCard: MaterialCardView
    private lateinit var feedbackTitle: TextView
    private lateinit var feedbackExplanation: TextView
    private lateinit var nextButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        WindowInsetsHelper.applySystemBarsPadding(this)

        bindViews()
        setupActions()
        observeState()
    }

    private fun bindViews() {
        quizTopicTitle = findViewById(R.id.quizTopicTitle)
        questionCounter = findViewById(R.id.questionCounter)
        scoreCounter = findViewById(R.id.scoreCounter)
        questionProgress = findViewById(R.id.questionProgress)
        timerText = findViewById(R.id.timerText)
        questionText = findViewById(R.id.questionText)
        feedbackCard = findViewById(R.id.feedbackCard)
        feedbackTitle = findViewById(R.id.feedbackTitle)
        feedbackExplanation = findViewById(R.id.feedbackExplanation)
        nextButton = findViewById(R.id.nextButton)
        optionButtons = listOf(
            findViewById(R.id.optionA),
            findViewById(R.id.optionB),
            findViewById(R.id.optionC),
            findViewById(R.id.optionD)
        )
    }

    private fun setupActions() {
        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.selectAnswer(index)
            }
        }

        nextButton.setOnClickListener {
            viewModel.nextQuestion()
        }
    }

    private fun observeState() {
        viewModel.uiState.observe(this) { state ->
            render(state)
        }

        viewModel.finishEvent.observe(this) { event ->
            if (event == null) return@observe
            startActivity(
                Intent(this, ResultActivity::class.java)
                    .putExtra(ResultActivity.EXTRA_TOPIC_ID, event.topicId)
                    .putExtra(ResultActivity.EXTRA_QUESTION_IDS, event.questionIds)
                    .putExtra(ResultActivity.EXTRA_SELECTED_ANSWERS, event.selectedAnswers)
                    .putExtra(ResultActivity.EXTRA_TIMEOUTS, event.timeouts)
                    .putExtra(ResultActivity.EXTRA_SCORE, event.score)
                    .putExtra(ResultActivity.EXTRA_TOTAL, event.total)
                    .putExtra(ResultActivity.EXTRA_DURATION_MILLIS, event.durationMillis)
            )
            viewModel.onFinishHandled()
            finish()
        }
    }

    private fun render(state: QuizUiState) {
        quizTopicTitle.text = state.topicTitle
        questionCounter.text = getString(R.string.question_counter, state.questionNumber, state.totalQuestions)
        scoreCounter.text = getString(R.string.score_counter, state.score)
        questionProgress.max = state.totalQuestions
        questionProgress.progress = state.questionNumber
        timerText.text = getString(R.string.timer_format, state.secondsRemaining)
        timerText.setTextColor(timerColor(state.secondsRemaining))
        questionText.text = state.questionText

        optionButtons.forEachIndexed { index, button ->
            renderOptionButton(button, index, state)
        }

        feedbackCard.visibility = if (state.isAnswered) View.VISIBLE else View.GONE
        feedbackTitle.text = feedbackTitleFor(state.feedbackType)
        feedbackExplanation.text = state.explanation.orEmpty()
        feedbackCard.setCardBackgroundColor(feedbackColor(state.feedbackType))

        nextButton.isEnabled = state.isAnswered
        nextButton.text = if (state.isLastQuestion) {
            getString(R.string.show_result)
        } else {
            getString(R.string.next_question)
        }
    }

    private fun renderOptionButton(button: MaterialButton, index: Int, state: QuizUiState) {
        val optionText = state.options.getOrNull(index).orEmpty()
        button.text = optionText
        button.isClickable = !state.isAnswered

        val isCorrect = state.correctIndex == index
        val isWrongSelection = state.selectedIndex == index && state.correctIndex != index
        val backgroundColor = when {
            state.isAnswered && isCorrect -> ContextCompat.getColor(this, R.color.success_container)
            state.isAnswered && isWrongSelection -> ContextCompat.getColor(this, R.color.error_container)
            else -> themeColor(com.google.android.material.R.attr.colorSurface)
        }
        val foregroundColor = when {
            state.isAnswered && isCorrect -> ContextCompat.getColor(this, R.color.on_success_container)
            state.isAnswered && isWrongSelection -> ContextCompat.getColor(this, R.color.on_error_container)
            else -> themeColor(com.google.android.material.R.attr.colorOnSurface)
        }
        val strokeColor = when {
            state.isAnswered && isCorrect -> ContextCompat.getColor(this, R.color.on_success_container)
            state.isAnswered && isWrongSelection -> ContextCompat.getColor(this, R.color.on_error_container)
            else -> themeColor(com.google.android.material.R.attr.colorOutline)
        }

        button.backgroundTintList = ColorStateList.valueOf(backgroundColor)
        button.strokeColor = ColorStateList.valueOf(strokeColor)
        button.setTextColor(foregroundColor)
        button.iconTint = ColorStateList.valueOf(foregroundColor)
        button.icon = when {
            state.isAnswered && isCorrect -> ContextCompat.getDrawable(this, R.drawable.ic_check_24)
            state.isAnswered && isWrongSelection -> ContextCompat.getDrawable(this, R.drawable.ic_close_24)
            else -> null
        }
    }

    private fun feedbackTitleFor(type: FeedbackType?): String {
        return when (type) {
            FeedbackType.CORRECT -> getString(R.string.feedback_correct)
            FeedbackType.WRONG -> getString(R.string.feedback_wrong)
            FeedbackType.TIMEOUT -> getString(R.string.feedback_timeout)
            null -> ""
        }
    }

    private fun feedbackColor(type: FeedbackType?): Int {
        return when (type) {
            FeedbackType.CORRECT -> ContextCompat.getColor(this, R.color.success_container)
            FeedbackType.WRONG -> ContextCompat.getColor(this, R.color.error_container)
            FeedbackType.TIMEOUT -> ContextCompat.getColor(this, R.color.warning_container)
            null -> themeColor(com.google.android.material.R.attr.colorSurfaceVariant)
        }
    }

    private fun timerColor(secondsRemaining: Int): Int {
        return if (secondsRemaining <= 5) {
            ContextCompat.getColor(this, R.color.on_error_container)
        } else {
            themeColor(com.google.android.material.R.attr.colorOnSurface)
        }
    }

    private fun themeColor(attr: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    companion object {
        const val EXTRA_TOPIC_ID = "extra_topic_id"
    }
}
