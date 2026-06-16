package com.example.kotlinquiz.ui.result

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinquiz.R
import com.example.kotlinquiz.data.QuizTopic
import com.example.kotlinquiz.ui.home.MainActivity
import com.example.kotlinquiz.ui.quiz.QuizActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator

class ResultActivity : AppCompatActivity() {
    private val params: ResultParams by lazy {
        ResultParams(
            topicId = intent.getStringExtra(EXTRA_TOPIC_ID) ?: QuizTopic.KOTLIN_BASICS.id,
            questionIds = intent.getIntArrayExtra(EXTRA_QUESTION_IDS) ?: intArrayOf(),
            selectedAnswers = intent.getIntArrayExtra(EXTRA_SELECTED_ANSWERS) ?: intArrayOf(),
            timeouts = intent.getBooleanArrayExtra(EXTRA_TIMEOUTS) ?: booleanArrayOf(),
            score = intent.getIntExtra(EXTRA_SCORE, 0),
            total = intent.getIntExtra(EXTRA_TOTAL, 0),
            durationMillis = intent.getLongExtra(EXTRA_DURATION_MILLIS, 0L)
        )
    }
    private val viewModel: ResultViewModel by viewModels {
        ResultViewModel.Factory(application, params)
    }

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var resultTitle: TextView
    private lateinit var resultScore: TextView
    private lateinit var resultPercent: TextView
    private lateinit var resultDuration: TextView
    private lateinit var resultProgress: LinearProgressIndicator
    private lateinit var highScoreBadge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        bindViews()
        setupReviewList()
        setupActions()
        observeState()
    }

    private fun bindViews() {
        resultTitle = findViewById(R.id.resultTitle)
        resultScore = findViewById(R.id.resultScore)
        resultPercent = findViewById(R.id.resultPercent)
        resultDuration = findViewById(R.id.resultDuration)
        resultProgress = findViewById(R.id.resultProgress)
        highScoreBadge = findViewById(R.id.highScoreBadge)
    }

    private fun setupReviewList() {
        reviewAdapter = ReviewAdapter()
        findViewById<RecyclerView>(R.id.reviewRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = reviewAdapter
        }
    }

    private fun setupActions() {
        findViewById<MaterialButton>(R.id.retryButton).setOnClickListener {
            startActivity(
                Intent(this, QuizActivity::class.java)
                    .putExtra(QuizActivity.EXTRA_TOPIC_ID, params.topicId)
            )
            finish()
        }

        findViewById<MaterialButton>(R.id.homeButton).setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
            finish()
        }
    }

    private fun observeState() {
        viewModel.uiState.observe(this) { state ->
            render(state)
        }
    }

    private fun render(state: ResultUiState) {
        resultTitle.text = state.title
        resultScore.text = state.scoreText
        resultPercent.text = getString(R.string.percent_format, state.percent)
        resultDuration.text = getString(R.string.duration_label) + ": " + state.durationText
        resultProgress.progress = state.percent
        highScoreBadge.text = state.highScoreText
        reviewAdapter.submitList(state.reviewItems)
    }

    companion object {
        const val EXTRA_TOPIC_ID = "extra_topic_id"
        const val EXTRA_QUESTION_IDS = "extra_question_ids"
        const val EXTRA_SELECTED_ANSWERS = "extra_selected_answers"
        const val EXTRA_TIMEOUTS = "extra_timeouts"
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_TOTAL = "extra_total"
        const val EXTRA_DURATION_MILLIS = "extra_duration_millis"
    }
}
