package com.example.kotlinquiz.data

enum class QuizTopic(
    val id: String,
    val title: String,
    val description: String
) {
    KOTLIN_BASICS(
        id = "kotlin_basics",
        title = "Kotlin Basics",
        description = "Cú pháp, null safety, collection và data class"
    ),
    ANDROID_UI(
        id = "android_ui",
        title = "Android UI",
        description = "Activity, Intent, RecyclerView và giao diện XML"
    );

    companion object {
        fun fromId(id: String?): QuizTopic {
            return values().firstOrNull { it.id == id } ?: KOTLIN_BASICS
        }
    }
}
