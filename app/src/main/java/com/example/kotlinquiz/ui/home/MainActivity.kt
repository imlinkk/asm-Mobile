package com.example.kotlinquiz.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinquiz.R
import com.example.kotlinquiz.data.QuizTopic
import com.example.kotlinquiz.ui.common.ThemeManager
import com.example.kotlinquiz.ui.common.WindowInsetsHelper
import com.example.kotlinquiz.ui.quiz.QuizActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch

class MainActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private var selectedTopicId = QuizTopic.KOTLIN_BASICS.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowInsetsHelper.applySystemBarsPadding(this)

        setupTopicPicker()
        setupDarkModeSwitch()
        setupActions()
        observeState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun setupTopicPicker() {
        val topicButtons = listOf(
            findViewById<MaterialButton>(R.id.kotlinTopicButton),
            findViewById<MaterialButton>(R.id.kotlinAdvancedTopicButton),
            findViewById<MaterialButton>(R.id.androidTopicButton)
        )

        fun updateSelection() {
            topicButtons.forEach { button ->
                button.isChecked = topicIdForButton(button.id) == selectedTopicId
            }
        }

        topicButtons.forEach { button ->
            button.setOnClickListener {
                selectedTopicId = topicIdForButton(button.id)
                updateSelection()
            }
        }

        updateSelection()
    }

    private fun setupDarkModeSwitch() {
        val darkModeSwitch = findViewById<MaterialSwitch>(R.id.darkModeSwitch)
        darkModeSwitch.isChecked = ThemeManager.isDarkMode(this)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            ThemeManager.setDarkMode(this, isChecked)
        }
    }

    private fun setupActions() {
        findViewById<MaterialButton>(R.id.startButton).setOnClickListener {
            startActivity(
                Intent(this, QuizActivity::class.java)
                    .putExtra(QuizActivity.EXTRA_TOPIC_ID, selectedTopicId)
            )
        }

        findViewById<MaterialButton>(R.id.highScoreButton).setOnClickListener {
            showHighScoreDialog()
        }
    }

    private fun observeState() {
        val highScoreSummary = findViewById<TextView>(R.id.highScoreSummary)
        viewModel.uiState.observe(this) { state ->
            highScoreSummary.text = state.bestOverallText
        }
    }

    private fun showHighScoreDialog() {
        val state = viewModel.uiState.value ?: return
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.high_score_title)
            .setMessage(state.highScoreLines.joinToString(separator = "\n"))
            .setPositiveButton(R.string.dialog_close, null)
            .show()
    }

    private fun topicIdForButton(buttonId: Int): String {
        return when (buttonId) {
            R.id.kotlinAdvancedTopicButton -> QuizTopic.KOTLIN_ADVANCED.id
            R.id.androidTopicButton -> QuizTopic.ANDROID_UI.id
            else -> QuizTopic.KOTLIN_BASICS.id
        }
    }
}
