package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AppStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("thinkx_prefs", Context.MODE_PRIVATE)

    // User Profile
    fun getUserProfile(): UserProfile {
        val username = prefs.getString("user_name", "Guest Player") ?: "Guest Player"
        val initials = prefs.getString("user_initials", "GU") ?: "GU"
        val classLevel = prefs.getString("user_class", "10") ?: "10"
        val xp = prefs.getInt("user_xp", 0)
        val streak = prefs.getInt("user_streak", 1)
        val lastActive = prefs.getString("user_last_active", "") ?: ""
        val totalQuizzes = prefs.getInt("user_quizzes", 0)
        val totalQuestions = prefs.getInt("user_total_q", 0)
        val totalCorrect = prefs.getInt("user_correct_q", 0)
        val themeName = prefs.getString("user_theme", GlassTheme.LIQUID_PORTAL.name)
        val theme = try {
            GlassTheme.valueOf(themeName ?: GlassTheme.LIQUID_PORTAL.name)
        } catch (e: Exception) {
            GlassTheme.LIQUID_PORTAL
        }

        return UserProfile(
            username = username,
            avatarInitials = initials,
            classLevel = classLevel,
            xp = xp,
            streak = streak,
            lastActiveDate = lastActive,
            totalQuizzes = totalQuizzes,
            totalQuestionsAnswered = totalQuestions,
            totalCorrect = totalCorrect,
            theme = theme
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString("user_name", profile.username)
            putString("user_initials", profile.avatarInitials)
            putString("user_class", profile.classLevel)
            putInt("user_xp", profile.xp)
            putInt("user_streak", profile.streak)
            putString("user_last_active", profile.lastActiveDate)
            putInt("user_quizzes", profile.totalQuizzes)
            putInt("user_total_q", profile.totalQuestionsAnswered)
            putInt("user_correct_q", profile.totalCorrect)
            putString("user_theme", profile.theme.name)
            apply()
        }
    }

    fun updateStreak(): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString("user_last_active", "") ?: ""
        var currentStreak = prefs.getInt("user_streak", 1)

        if (lastDate.isEmpty()) {
            currentStreak = 1
            prefs.edit().putString("user_last_active", today).putInt("user_streak", currentStreak).apply()
        } else if (lastDate != today) {
            // Check if yesterday
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val lastD = sdf.parse(lastDate)
                val cal = Calendar.getInstance()
                cal.time = Date()
                cal.add(Calendar.DAY_OF_YEAR, -1)
                val yesterday = sdf.format(cal.time)
                if (lastDate == yesterday) {
                    currentStreak += 1
                } else {
                    currentStreak = 1
                }
            } catch (e: Exception) {
                currentStreak = 1
            }
            prefs.edit().putString("user_last_active", today).putInt("user_streak", currentStreak).apply()
        }
        return currentStreak
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("is_logged_in", loggedIn).apply()
    }

    // Quiz Configuration Persistence
    fun getQuizConfig(): QuizConfig {
        val classLevel = prefs.getString("cfg_class", "10") ?: "10"
        val subject = prefs.getString("cfg_subject", "Science") ?: "Science"
        val chapter = prefs.getString("cfg_chapter", "All Chapters (Full Syllabus)") ?: "All Chapters (Full Syllabus)"
        val count = prefs.getInt("cfg_count", 10)
        val opts = prefs.getInt("cfg_opts", 4)
        val timer = prefs.getInt("cfg_timer", 30)
        val diffName = prefs.getString("cfg_diff", Difficulty.MODERATE.name)
        val diff = try {
            Difficulty.valueOf(diffName ?: Difficulty.MODERATE.name)
        } catch (e: Exception) {
            Difficulty.MODERATE
        }
        return QuizConfig(classLevel, subject, chapter, count, opts, timer, diff)
    }

    fun saveQuizConfig(cfg: QuizConfig) {
        prefs.edit().apply {
            putString("cfg_class", cfg.classLevel)
            putString("cfg_subject", cfg.subject)
            putString("cfg_chapter", cfg.chapter)
            putInt("cfg_count", cfg.questionCount)
            putInt("cfg_opts", cfg.optionsCount)
            putInt("cfg_timer", cfg.timerSecondsPerQuestion)
            putString("cfg_diff", cfg.difficulty.name)
            apply()
        }
    }

    // Results History
    fun saveQuizResult(result: QuizResult) {
        val raw = prefs.getString("quiz_results_json", "[]") ?: "[]"
        val array = try { JSONArray(raw) } catch (e: Exception) { JSONArray() }
        val obj = JSONObject().apply {
            put("id", result.id)
            put("score", result.score)
            put("totalQuestions", result.totalQuestions)
            put("percentage", result.percentage)
            put("correctCount", result.correctCount)
            put("incorrectCount", result.incorrectCount)
            put("skippedCount", result.skippedCount)
            put("timeSpentSeconds", result.timeSpentSeconds)
            put("subject", result.subject)
            put("chapter", result.chapter)
            put("difficulty", result.difficulty)
            put("xpEarned", result.xpEarned)
            put("timestamp", result.timestamp)
        }
        array.put(obj)
        prefs.edit().putString("quiz_results_json", array.toString()).apply()
    }

    fun getQuizResults(): List<QuizResult> {
        val raw = prefs.getString("quiz_results_json", "[]") ?: "[]"
        val list = mutableListOf<QuizResult>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    QuizResult(
                        id = obj.optString("id"),
                        score = obj.optInt("score"),
                        totalQuestions = obj.optInt("totalQuestions"),
                        percentage = obj.optInt("percentage"),
                        correctCount = obj.optInt("correctCount"),
                        incorrectCount = obj.optInt("incorrectCount"),
                        skippedCount = obj.optInt("skippedCount"),
                        timeSpentSeconds = obj.optInt("timeSpentSeconds"),
                        subject = obj.optString("subject"),
                        chapter = obj.optString("chapter"),
                        difficulty = obj.optString("difficulty"),
                        xpEarned = obj.optInt("xpEarned"),
                        timestamp = obj.optLong("timestamp")
                    )
                )
            }
        } catch (e: Exception) {
            // gracefully return empty list
        }
        return list.reversed()
    }

    // Flashcards
    fun getFlashcards(): List<Flashcard> {
        val raw = prefs.getString("flashcards_json", null)
        if (raw == null) {
            // Return curated default flashcards
            val defaultCards = listOf(
                Flashcard("1", "Science", "Exothermic Reaction", "A chemical reaction that releases energy by light or heat. Example: Combustion of methane (CH4 + 2O2 -> CO2 + 2H2O + Heat)."),
                Flashcard("2", "Science", "Ohm's Law", "The electric current (I) flowing through a conductor is directly proportional to the potential difference (V) across its ends, provided temperature remains constant: V = IR."),
                Flashcard("3", "Physics", "Snell's Law of Refraction", "The ratio of the sine of the angle of incidence to the sine of the angle of refraction is a constant for a given pair of media: sin(i) / sin(r) = n2 / n1."),
                Flashcard("4", "Chemistry", "Electronegativity", "The tendency of an atom in a molecule to attract the shared pair of electrons towards itself. Fluorine is the most electronegative element (4.0)."),
                Flashcard("5", "Biology", "ATP (Adenosine Triphosphate)", "The primary energy currency of the cell, synthesized in mitochondria through cellular respiration and utilized during active transport and biosynthesis.")
            )
            saveFlashcards(defaultCards)
            return defaultCards
        }

        val list = mutableListOf<Flashcard>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Flashcard(
                        id = obj.optString("id"),
                        subject = obj.optString("subject"),
                        concept = obj.optString("concept"),
                        explanation = obj.optString("explanation"),
                        isCustom = obj.optBoolean("isCustom", false)
                    )
                )
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }

    fun saveFlashcards(cards: List<Flashcard>) {
        val array = JSONArray()
        for (c in cards) {
            val obj = JSONObject().apply {
                put("id", c.id)
                put("subject", c.subject)
                put("concept", c.concept)
                put("explanation", c.explanation)
                put("isCustom", c.isCustom)
            }
            array.put(obj)
        }
        prefs.edit().putString("flashcards_json", array.toString()).apply()
    }

    fun addCustomFlashcard(card: Flashcard) {
        val current = getFlashcards().toMutableList()
        current.add(0, card)
        saveFlashcards(current)
    }

    // Chat History
    fun getChatMessages(): List<ChatMessage> {
        val raw = prefs.getString("chat_messages_json", null)
        if (raw == null) {
            return listOf(
                ChatMessage(
                    id = "msg_init",
                    sender = MessageSender.AI,
                    text = "Welcome to ThinkX AI! I am your dedicated NCERT study companion. Ask me any conceptual question, chapter summary, numerical, or revision query."
                )
            )
        }
        val list = mutableListOf<ChatMessage>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    ChatMessage(
                        id = obj.optString("id"),
                        sender = if (obj.optString("sender") == "USER") MessageSender.USER else MessageSender.AI,
                        text = obj.optString("text"),
                        timestamp = obj.optLong("timestamp"),
                        imageUrl = if (obj.has("imageUrl")) obj.optString("imageUrl") else null
                    )
                )
            }
        } catch (e: Exception) {
            // ignore
        }
        return list
    }

    fun saveChatMessages(messages: List<ChatMessage>) {
        val array = JSONArray()
        for (m in messages) {
            val obj = JSONObject().apply {
                put("id", m.id)
                put("sender", m.sender.name)
                put("text", m.text)
                put("timestamp", m.timestamp)
                if (m.imageUrl != null) put("imageUrl", m.imageUrl)
            }
            array.put(obj)
        }
        prefs.edit().putString("chat_messages_json", array.toString()).apply()
    }

    fun clearChat() {
        prefs.edit().remove("chat_messages_json").apply()
    }

    // Mistake Bank Persistence
    fun getMistakes(): List<Mistake> {
        val raw = prefs.getString("mistakes_bank_json", null)
        if (raw == null) {
            // Seed a couple of realistic mistakes for initial curriculum exploration if empty
            val initial = listOf(
                Mistake(
                    question = "Why is respiration considered an exothermic process?",
                    options = listOf(
                        "Heat is absorbed during glycolysis",
                        "Glucose combines with oxygen in cells to release energy",
                        "Carbon dioxide is consumed",
                        "Water molecules split"
                    ),
                    selectedOptionIndex = 0,
                    correctOptionIndex = 1,
                    explanation = "During digestion, food is broken down into glucose. This glucose combines with oxygen in our cells and releases energy, making it an exothermic reaction.",
                    classLevel = "10",
                    subject = "Science",
                    chapter = "Chemical Reactions and Equations",
                    difficulty = "Moderate",
                    timesAttempted = 1,
                    timesCorrected = 0
                )
            )
            saveMistakes(initial)
            return initial
        }

        val list = mutableListOf<Mistake>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val opts = mutableListOf<String>()
                val optArr = obj.optJSONArray("options")
                if (optArr != null) {
                    for (j in 0 until optArr.length()) {
                        opts.add(optArr.getString(j))
                    }
                }
                list.add(
                    Mistake(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        question = obj.optString("question"),
                        options = opts,
                        selectedOptionIndex = obj.optInt("selectedOptionIndex", 0),
                        correctOptionIndex = obj.optInt("correctOptionIndex", 0),
                        explanation = obj.optString("explanation"),
                        classLevel = obj.optString("classLevel", "10"),
                        subject = obj.optString("subject", "Science"),
                        chapter = obj.optString("chapter", "Chemical Reactions and Equations"),
                        difficulty = obj.optString("difficulty", "Moderate"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        timesAttempted = obj.optInt("timesAttempted", 1),
                        timesCorrected = obj.optInt("timesCorrected", 0)
                    )
                )
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }

    fun saveMistakes(mistakes: List<Mistake>) {
        val array = JSONArray()
        for (m in mistakes) {
            val obj = JSONObject().apply {
                put("id", m.id)
                put("question", m.question)
                put("options", JSONArray(m.options))
                put("selectedOptionIndex", m.selectedOptionIndex)
                put("correctOptionIndex", m.correctOptionIndex)
                put("explanation", m.explanation)
                put("classLevel", m.classLevel)
                put("subject", m.subject)
                put("chapter", m.chapter)
                put("difficulty", m.difficulty)
                put("timestamp", m.timestamp)
                put("timesAttempted", m.timesAttempted)
                put("timesCorrected", m.timesCorrected)
            }
            array.put(obj)
        }
        prefs.edit().putString("mistakes_bank_json", array.toString()).apply()
    }

    fun recordMistake(
        question: Question,
        selectedIdx: Int,
        classLevel: String,
        subject: String,
        chapter: String
    ) {
        val current = getMistakes().toMutableList()
        val existingIndex = current.indexOfFirst { it.question == question.question }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(
                timesAttempted = existing.timesAttempted + 1,
                selectedOptionIndex = selectedIdx,
                timestamp = System.currentTimeMillis()
            )
        } else {
            current.add(
                0,
                Mistake(
                    question = question.question,
                    options = question.options,
                    selectedOptionIndex = selectedIdx,
                    correctOptionIndex = question.correctAnswerIndex,
                    explanation = question.explanation,
                    classLevel = classLevel,
                    subject = subject,
                    chapter = chapter,
                    difficulty = question.difficulty.label
                )
            )
        }
        saveMistakes(current)
    }

    fun recordMistakeCorrected(questionText: String) {
        val current = getMistakes().toMutableList()
        val idx = current.indexOfFirst { it.question == questionText }
        if (idx >= 0) {
            val existing = current[idx]
            current[idx] = existing.copy(
                timesCorrected = existing.timesCorrected + 1,
                timestamp = System.currentTimeMillis()
            )
            saveMistakes(current)
        }
    }

    // Daily Pulse Tracking
    fun recordDailyQuizActivity(xpEarned: Int, questionsAnswered: Int, correctCount: Int) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString("pulse_date", "") ?: ""

        if (lastDate != today) {
            prefs.edit().apply {
                putString("pulse_date", today)
                putInt("pulse_xp", xpEarned)
                putInt("pulse_quizzes", 1)
                putInt("pulse_q_count", questionsAnswered)
                putInt("pulse_correct", correctCount)
                apply()
            }
        } else {
            val currentXp = prefs.getInt("pulse_xp", 0) + xpEarned
            val currentQuizzes = prefs.getInt("pulse_quizzes", 0) + 1
            val currentQCount = prefs.getInt("pulse_q_count", 0) + questionsAnswered
            val currentCorrect = prefs.getInt("pulse_correct", 0) + correctCount
            prefs.edit().apply {
                putInt("pulse_xp", currentXp)
                putInt("pulse_quizzes", currentQuizzes)
                putInt("pulse_q_count", currentQCount)
                putInt("pulse_correct", currentCorrect)
                apply()
            }
        }
    }

    fun getDailyPulse(): DailyPulse {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString("pulse_date", "") ?: ""
        val streak = prefs.getInt("user_streak", 1)

        val (xpToday, quizzesToday, qToday, correctToday) = if (lastDate == today) {
            listOf(
                prefs.getInt("pulse_xp", 0),
                prefs.getInt("pulse_quizzes", 0),
                prefs.getInt("pulse_q_count", 0),
                prefs.getInt("pulse_correct", 0)
            )
        } else {
            listOf(0, 0, 0, 0)
        }

        val accuracy = if (qToday > 0) (correctToday * 100) / qToday else 0
        val statusMsg = when {
            quizzesToday >= 3 -> "🔥 Momentum unlocked! Great consistency."
            quizzesToday >= 1 -> "⚡ On track today. Ready for the next milestone."
            else -> "✨ Start today's first session to lock your streak."
        }

        return DailyPulse(
            streak = streak,
            xpToday = xpToday,
            quizzesToday = quizzesToday,
            questionsToday = qToday,
            accuracyToday = accuracy,
            statusMessage = statusMsg
        )
    }

    // Last Studied Session (for "Continue Learning")
    fun saveLastStudiedSession(classLevel: String, subject: String, chapter: String) {
        prefs.edit().apply {
            putString("last_class", classLevel)
            putString("last_subject", subject)
            putString("last_chapter", chapter)
            putLong("last_time", System.currentTimeMillis())
            apply()
        }
    }

    fun getLastStudiedSession(defaultClass: String = "10"): LastStudiedSession {
        val cls = prefs.getString("last_class", defaultClass) ?: defaultClass
        val sub = prefs.getString("last_subject", "Science") ?: "Science"
        val ch = prefs.getString("last_chapter", "Chemical Reactions and Equations") ?: "Chemical Reactions and Equations"
        val time = prefs.getLong("last_time", System.currentTimeMillis())
        return LastStudiedSession(classLevel = cls, subject = sub, chapter = ch, timestamp = time)
    }

    // Today's Study Plan
    fun getStudyPlan(classLevel: String, subject: String, chapter: String): List<StudyPlanItem> {
        val raw = prefs.getString("study_plan_json", null)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val planDate = prefs.getString("study_plan_date", "") ?: ""

        if (raw == null || planDate != today) {
            // Generate fresh personalized plan for the day
            val freshPlan = listOf(
                StudyPlanItem(
                    title = "Quick Concept Recall",
                    subtitle = "$subject • $chapter",
                    type = "REVISE",
                    subject = subject,
                    chapter = chapter,
                    xpReward = 20,
                    timeMinutes = 3
                ),
                StudyPlanItem(
                    title = "5Q Speed Practice",
                    subtitle = "Verify fundamentals in $subject",
                    type = "PRACTICE",
                    subject = subject,
                    chapter = chapter,
                    xpReward = 30,
                    timeMinutes = 4
                ),
                StudyPlanItem(
                    title = "Mistake Bank Audit",
                    subtitle = "Fix weak points from prior rounds",
                    type = "MISTAKES",
                    subject = subject,
                    chapter = chapter,
                    xpReward = 25,
                    timeMinutes = 5
                ),
                StudyPlanItem(
                    title = "Mastery Checkpoint",
                    subtitle = "Test exam readiness with ThinkX AI",
                    type = "AI_TUTOR",
                    subject = subject,
                    chapter = chapter,
                    xpReward = 40,
                    timeMinutes = 5
                )
            )
            saveStudyPlan(freshPlan)
            prefs.edit().putString("study_plan_date", today).apply()
            return freshPlan
        }

        val list = mutableListOf<StudyPlanItem>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    StudyPlanItem(
                        id = obj.optString("id"),
                        title = obj.optString("title"),
                        subtitle = obj.optString("subtitle"),
                        type = obj.optString("type"),
                        subject = obj.optString("subject"),
                        chapter = obj.optString("chapter"),
                        isCompleted = obj.optBoolean("isCompleted", false),
                        xpReward = obj.optInt("xpReward", 25),
                        timeMinutes = obj.optInt("timeMinutes", 5)
                    )
                )
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }

    fun saveStudyPlan(plan: List<StudyPlanItem>) {
        val array = JSONArray()
        for (item in plan) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("title", item.title)
                put("subtitle", item.subtitle)
                put("type", item.type)
                put("subject", item.subject)
                put("chapter", item.chapter)
                put("isCompleted", item.isCompleted)
                put("xpReward", item.xpReward)
                put("timeMinutes", item.timeMinutes)
            }
            array.put(obj)
        }
        prefs.edit().putString("study_plan_json", array.toString()).apply()
    }

    fun toggleStudyPlanItem(id: String, currentPlan: List<StudyPlanItem>): List<StudyPlanItem> {
        val updated = currentPlan.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
        saveStudyPlan(updated)
        return updated
    }

    // Revision Packs
    fun getRevisionPacks(): List<RevisionPack> {
        val raw = prefs.getString("revision_packs_json", null)
        if (raw == null) return emptyList()
        val list = mutableListOf<RevisionPack>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val keyConcepts = mutableListOf<String>()
                val conceptsArr = obj.optJSONArray("keyConcepts")
                if (conceptsArr != null) {
                    for (j in 0 until conceptsArr.length()) keyConcepts.add(conceptsArr.getString(j))
                }
                val formulas = mutableListOf<String>()
                val formulaArr = obj.optJSONArray("formulas")
                if (formulaArr != null) {
                    for (j in 0 until formulaArr.length()) formulas.add(formulaArr.getString(j))
                }
                val defs = mutableListOf<DefinitionItem>()
                val defsArr = obj.optJSONArray("definitions")
                if (defsArr != null) {
                    for (j in 0 until defsArr.length()) {
                        val dObj = defsArr.getJSONObject(j)
                        defs.add(DefinitionItem(dObj.optString("term"), dObj.optString("definition")))
                    }
                }
                val mistakes = mutableListOf<String>()
                val misArr = obj.optJSONArray("commonMistakes")
                if (misArr != null) {
                    for (j in 0 until misArr.length()) mistakes.add(misArr.getString(j))
                }

                list.add(
                    RevisionPack(
                        id = obj.optString("id"),
                        classLevel = obj.optString("classLevel"),
                        subject = obj.optString("subject"),
                        chapter = obj.optString("chapter"),
                        summary = obj.optString("summary"),
                        keyConcepts = keyConcepts,
                        formulas = formulas,
                        definitions = defs,
                        commonMistakes = mistakes,
                        timestamp = obj.optLong("timestamp")
                    )
                )
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }

    fun saveRevisionPack(pack: RevisionPack) {
        val current = getRevisionPacks().toMutableList()
        // Replace existing for same chapter or prepend
        current.removeAll { it.chapter == pack.chapter && it.subject == pack.subject }
        current.add(0, pack)

        val array = JSONArray()
        for (p in current) {
            val obj = JSONObject().apply {
                put("id", p.id)
                put("classLevel", p.classLevel)
                put("subject", p.subject)
                put("chapter", p.chapter)
                put("summary", p.summary)
                put("keyConcepts", JSONArray(p.keyConcepts))
                put("formulas", JSONArray(p.formulas))
                val defsArr = JSONArray()
                p.definitions.forEach { d ->
                    defsArr.put(JSONObject().apply {
                        put("term", d.term)
                        put("definition", d.definition)
                    })
                }
                put("definitions", defsArr)
                put("commonMistakes", JSONArray(p.commonMistakes))
                put("timestamp", p.timestamp)
            }
            array.put(obj)
        }
        prefs.edit().putString("revision_packs_json", array.toString()).apply()
    }

    fun addXp(amount: Int) {
        val profile = getUserProfile()
        saveUserProfile(profile.copy(xp = profile.xp + amount))
        recordDailyQuizActivity(xpEarned = amount, questionsAnswered = 0, correctCount = 0)
    }
}
