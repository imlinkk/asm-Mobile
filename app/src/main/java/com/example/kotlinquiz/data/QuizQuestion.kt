package com.example.kotlinquiz.data

data class QuizQuestion(
    val id: Int,
    val topic: QuizTopic,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)
