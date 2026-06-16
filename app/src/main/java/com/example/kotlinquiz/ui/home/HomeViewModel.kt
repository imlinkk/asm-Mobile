package com.example.kotlinquiz.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlinquiz.data.HighScoreStore
import com.example.kotlinquiz.data.QuizTopic
import com.example.kotlinquiz.ui.common.UiFormatters

data class HomeUiState(
    val bestOverallText: String,
    val highScoreLines: List<String>
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val highScoreStore = HighScoreStore(application)
    private val _uiState = MutableLiveData<HomeUiState>()
    val uiState: LiveData<HomeUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        val lines = QuizTopic.values().map { topic ->
            val highScore = highScoreStore.get(topic)
            if (highScore == null) {
                "${topic.title}: chưa có điểm"
            } else {
                "${topic.title}: ${highScore.score}/${highScore.total} - ${highScore.percent}% trong ${UiFormatters.duration(highScore.durationMillis)}"
            }
        }

        val best = highScoreStore.getBestOverall()
        val bestText = if (best == null) {
            "Điểm cao nhất: chưa có"
        } else {
            "Điểm cao nhất: ${best.topic.title} - ${best.highScore.score}/${best.highScore.total} (${best.highScore.percent}%)"
        }

        _uiState.value = HomeUiState(
            bestOverallText = bestText,
            highScoreLines = lines
        )
    }
}
