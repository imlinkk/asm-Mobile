package com.example.kotlinquiz

import android.app.Application
import com.example.kotlinquiz.ui.common.ThemeManager

class QuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeManager.applySaved(this)
    }
}
