package com.example.kotlinquiz.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinquiz.R
import com.google.android.material.card.MaterialCardView

class ReviewAdapter : ListAdapter<ReviewUiItem, ReviewAdapter.ReviewViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review_question, parent, false)
        return ReviewViewHolder(view as MaterialCardView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReviewViewHolder(
        private val card: MaterialCardView
    ) : RecyclerView.ViewHolder(card) {
        private val statusIcon: ImageView = card.findViewById(R.id.reviewStatusIcon)
        private val question: TextView = card.findViewById(R.id.reviewQuestion)
        private val yourAnswer: TextView = card.findViewById(R.id.reviewYourAnswer)
        private val correctAnswer: TextView = card.findViewById(R.id.reviewCorrectAnswer)
        private val explanation: TextView = card.findViewById(R.id.reviewExplanation)

        fun bind(item: ReviewUiItem) {
            val context = card.context
            val background = if (item.isCorrect) R.color.success_container else R.color.error_container
            val foreground = if (item.isCorrect) R.color.on_success_container else R.color.on_error_container
            val foregroundColor = ContextCompat.getColor(context, foreground)

            card.setCardBackgroundColor(ContextCompat.getColor(context, background))
            statusIcon.setImageResource(if (item.isCorrect) R.drawable.ic_check_24 else R.drawable.ic_close_24)
            statusIcon.setColorFilter(foregroundColor)
            question.setTextColor(foregroundColor)
            yourAnswer.setTextColor(foregroundColor)
            correctAnswer.setTextColor(foregroundColor)
            explanation.setTextColor(foregroundColor)

            question.text = "${item.questionNumber}. ${item.question}"
            yourAnswer.text = when {
                item.timedOut -> context.getString(R.string.timeout_answer)
                item.selectedAnswer != null -> context.getString(R.string.your_answer, item.selectedAnswer)
                else -> context.getString(R.string.no_answer)
            }
            correctAnswer.text = context.getString(R.string.correct_answer, item.correctAnswer)
            explanation.text = item.explanation
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ReviewUiItem>() {
        override fun areItemsTheSame(oldItem: ReviewUiItem, newItem: ReviewUiItem): Boolean {
            return oldItem.questionId == newItem.questionId
        }

        override fun areContentsTheSame(oldItem: ReviewUiItem, newItem: ReviewUiItem): Boolean {
            return oldItem == newItem
        }
    }
}
