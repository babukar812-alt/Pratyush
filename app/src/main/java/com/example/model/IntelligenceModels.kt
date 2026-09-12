package com.example.model

import java.util.UUID

/**
 * Intelligent Learning Ecosystem Models for ThinkX.
 */

data class Mistake(
    val id: String = UUID.randomUUID().toString(),
    val question: String,
    val options: List<String>,
    val selectedOptionIndex: Int,
    val correctOptionIndex: Int,
    val explanation: String,
    val classLevel: String,
    val subject: String,
    val chapter: String,
    val difficulty: String = "Moderate",
    val timestamp: Long = System.currentTimeMillis(),
    val timesAttempted: Int = 1,
    val timesCorrected: Int = 0
) {
    val status: String
        get() = when {
            timesCorrected >= 2 -> "Mastered"
            timesCorrected == 1 -> "Improving"
            else -> "Needs review"
        }
}

data class StudyPlanItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String,
    val type: String, // "PRACTICE", "MISTAKES", "REVISE", "CARDS", "AI_TUTOR"
    val subject: String,
    val chapter: String,
    val isCompleted: Boolean = false,
    val xpReward: Int = 25,
    val timeMinutes: Int = 5
)

data class RevisionPack(
    val id: String = UUID.randomUUID().toString(),
    val classLevel: String,
    val subject: String,
    val chapter: String,
    val summary: String,
    val keyConcepts: List<String>,
    val formulas: List<String>,
    val definitions: List<DefinitionItem>,
    val commonMistakes: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)

data class DefinitionItem(
    val term: String,
    val definition: String
)

data class ChapterMastery(
    val classLevel: String,
    val subject: String,
    val chapter: String,
    val overallScore: Int, // 0 - 100
    val conceptsMastered: Int,
    val totalConcepts: Int,
    val quizzesAttempted: Int,
    val accuracyPercent: Int,
    val activeMistakesCount: Int,
    val statusLabel: String // "Unstarted", "Needs Practice", "Good Progress", "Mastered"
)

data class DailyPulse(
    val streak: Int,
    val xpToday: Int,
    val quizzesToday: Int,
    val questionsToday: Int,
    val accuracyToday: Int,
    val statusMessage: String
)

data class LastStudiedSession(
    val classLevel: String,
    val subject: String,
    val chapter: String,
    val timestamp: Long = System.currentTimeMillis(),
    val masteryPercent: Int = 50
)

data class SmartRecommendation(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val actionLabel: String,
    val type: RecommendationType,
    val subject: String,
    val chapter: String
)

enum class RecommendationType {
    PRACTICE_MISTAKES,
    CONTINUE_LEARNING,
    QUICK_PRACTICE,
    REVISE_WEAK_AREA,
    GENERATE_REVISION_PACK,
    CHALLENGE_ME
}

enum class ConceptMasteryState(val label: String) {
    NOT_STARTED("Not Started"),
    LEARNING("Learning"),
    DEVELOPING("Developing"),
    STRONG("Strong"),
    MASTERED("Mastered")
}

data class ConceptMasteryItem(
    val id: String = UUID.randomUUID().toString(),
    val conceptName: String,
    val chapterName: String,
    val subjectName: String,
    val classLevel: String,
    val masteryPercent: Int, // 0 - 100
    val state: ConceptMasteryState,
    val mistakesCount: Int,
    val quizzesCount: Int
)

data class UserLearningProfile(
    val classLevel: String,
    val subjectsStudied: List<String>,
    val chaptersStudied: List<String>,
    val totalQuizAttempts: Int,
    val averageScore: Int,
    val totalMistakesRecorded: Int,
    val activeMistakesCount: Int,
    val mistakesMastered: Int,
    val flashcardsReviewedCount: Int,
    val revisionPacksCount: Int,
    val studySessionsCount: Int,
    val approximateStudyMinutes: Int,
    val streak: Int,
    val xp: Int,
    val overallMasteryPercent: Int,
    val lastOpenedChapter: String,
    val weakAreas: List<String>,
    val recommendedDifficulty: Difficulty
)

data class WeeklyReport(
    val totalSessions: Int,
    val studyTimeMinutes: Int,
    val quizzesCompleted: Int,
    val averageScore: Int,
    val strongestSubject: String,
    val weakestSubject: String,
    val strongestChapter: String,
    val weakestChapter: String,
    val mistakesImproved: Int,
    val masteryGainedPercent: Int,
    val xpEarned: Int,
    val streak: Int,
    val insight: String,
    val isEligible: Boolean
)

enum class ChallengeType(val label: String, val subtitle: String) {
    WEAK_AREA("Weak Area Challenge", "Target concepts with past mistakes"),
    SPEED("Speed Challenge", "15s rapid-fire NCERT questions"),
    PERFECT_SCORE("Perfect Score Challenge", "Strive for a 100% flawless round"),
    MIXED_SUBJECT("Mixed Subject Challenge", "Multi-disciplinary cross-subject quiz"),
    HARD_MODE("Hard Mode", "High difficulty Olympiad-grade questions"),
    MISTAKE("Mistake Bank Drill", "Re-attempt questions you previously missed"),
    CHAPTER("Chapter Mastery Challenge", "Deep mastery check for the current chapter")
}

data class ChallengeConfig(
    val type: ChallengeType,
    val title: String,
    val subtitle: String,
    val description: String,
    val subject: String,
    val chapter: String,
    val questionCount: Int,
    val timerSecondsPerQuestion: Int,
    val difficulty: Difficulty,
    val xpReward: Int,
    val classLevel: String = "10",
    val isExamMode: Boolean = false
)

enum class VoiceTutorMode(val label: String, val promptPrefix: String) {
    EXPLAIN("Explain", "Explain this NCERT concept verbally and simply: "),
    PRACTICE("Practice", "Quiz me verbally with a single question on: "),
    RAPID_REVISION("Rapid Revision", "Provide a fast 60-second verbal summary with key formulas for: "),
    ASK_ME_QUESTIONS("Ask Me Questions", "Ask me an insightful conceptual question about: "),
    EXAM_PRACTICE("Exam Practice", "Give me a high-yield board exam oral question with evaluation criteria on: ")
}

