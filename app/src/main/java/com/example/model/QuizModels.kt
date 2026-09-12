package com.example.model

enum class Difficulty(val label: String) {
    FOUNDATION("Foundation"),
    EASY("Easy"),
    MODERATE("Moderate"),
    HIGH("Hard"),
    ADVANCED("Advanced"),
    OLYMPIAD("Olympiad (Max)"),
    ADAPTIVE("Adaptive (Auto)")
}

data class Question(
    val id: String,
    val classLevel: String = "10",
    val subject: String = "Science",
    val chapterId: String = "",
    val chapterName: String = "",
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val difficulty: Difficulty = Difficulty.MODERATE,
    val isAssertionReason: Boolean = false,
    val chapter: String = chapterName.ifEmpty { "General" }
)

data class QuizConfig(
    val classLevel: String = "10",
    val subject: String = "Science",
    val chapter: String = "All Chapters (Full Syllabus)",
    val questionCount: Int = 10,
    val optionsCount: Int = 4,
    val timerSecondsPerQuestion: Int = 30,
    val difficulty: Difficulty = Difficulty.MODERATE,
    val isExamMode: Boolean = false
)

data class QuizResult(
    val id: String = System.currentTimeMillis().toString(),
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val timeSpentSeconds: Int,
    val subject: String,
    val chapter: String,
    val difficulty: String,
    val xpEarned: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val classLevel: String = "10"
)
