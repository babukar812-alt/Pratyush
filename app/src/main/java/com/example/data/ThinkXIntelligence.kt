package com.example.data

import com.example.model.*

/**
 * Intelligent Academic Engine powering ThinkX Recommendations,
 * Chapter Mastery Calculations, Revision Packs, and Smart Search.
 */
object ThinkXIntelligence {

    fun calculateChapterMastery(
        classLevel: String,
        subject: String,
        chapter: String,
        storage: AppStorage
    ): ChapterMastery {
        val results = storage.getQuizResults().filter {
            it.subject.equals(subject, ignoreCase = true) &&
            (it.chapter.equals(chapter, ignoreCase = true) || it.chapter.contains("All Chapters", ignoreCase = true))
        }

        val mistakes = storage.getMistakes().filter {
            it.subject.equals(subject, ignoreCase = true) &&
            (it.chapter.equals(chapter, ignoreCase = true) || chapter.contains("All Chapters", ignoreCase = true))
        }

        val activeMistakes = mistakes.count { it.status == "Needs review" }
        val masteredMistakes = mistakes.count { it.status == "Mastered" }

        val totalConcepts = 6
        val quizzesAttempted = results.size

        val avgAccuracy = if (results.isNotEmpty()) {
            results.map { it.percentage }.average().toInt()
        } else {
            0
        }

        // Weighted mastery formula: 50% quiz performance + 30% mistake resolution + 20% activity volume
        val calculatedPercent = if (quizzesAttempted == 0) {
            if (activeMistakes > 0) 30 else 0
        } else {
            val perfWeight = (avgAccuracy * 0.5f).toInt()
            val mistakeWeight = if (mistakes.isNotEmpty()) {
                ((masteredMistakes.toFloat() / mistakes.size) * 30f).toInt()
            } else {
                25
            }
            val volumeWeight = (quizzesAttempted.coerceAtMost(5) * 4) // max 20%
            (perfWeight + mistakeWeight + volumeWeight).coerceIn(10, 100)
        }

        val conceptsMastered = ((calculatedPercent / 100f) * totalConcepts).toInt().coerceIn(0, totalConcepts)

        val statusLabel = when {
            calculatedPercent >= 85 -> "Mastered"
            calculatedPercent >= 65 -> "Good Progress"
            calculatedPercent >= 40 -> "Needs Practice"
            quizzesAttempted > 0 -> "Needs Focus"
            else -> "Unstarted"
        }

        return ChapterMastery(
            classLevel = classLevel,
            subject = subject,
            chapter = chapter,
            overallScore = calculatedPercent,
            conceptsMastered = conceptsMastered,
            totalConcepts = totalConcepts,
            quizzesAttempted = quizzesAttempted,
            accuracyPercent = avgAccuracy,
            activeMistakesCount = activeMistakes,
            statusLabel = statusLabel
        )
    }

    fun getSmartRecommendations(
        storage: AppStorage,
        currentClass: String,
        currentSubject: String
    ): List<SmartRecommendation> {
        val list = mutableListOf<SmartRecommendation>()
        val mistakes = storage.getMistakes().filter { it.status == "Needs review" }

        // 1. If mistakes exist, highest priority is to fix them
        if (mistakes.isNotEmpty()) {
            val count = mistakes.size
            val topSubject = mistakes.first().subject
            val topChapter = mistakes.first().chapter
            list.add(
                SmartRecommendation(
                    title = "Fix $count Active Mistake${if (count > 1) "s" else ""}",
                    description = "Target questions you previously missed in $topSubject to avoid exam mark deductions.",
                    actionLabel = "Practice Mistakes",
                    type = RecommendationType.PRACTICE_MISTAKES,
                    subject = topSubject,
                    chapter = topChapter
                )
            )
        }

        // 2. Continue learning from last active chapter
        val last = storage.getLastStudiedSession(currentClass)
        list.add(
            SmartRecommendation(
                title = "Continue with ${last.chapter}",
                description = "Pick up right where you left off in ${last.subject} (Class ${last.classLevel}).",
                actionLabel = "Continue Round",
                type = RecommendationType.CONTINUE_LEARNING,
                subject = last.subject,
                chapter = last.chapter
            )
        )

        // 3. Quick high-yield practice
        list.add(
            SmartRecommendation(
                title = "3-Minute Power Drill",
                description = "Solve 5 rapid NCERT questions in $currentSubject to maintain recall speed.",
                actionLabel = "Quick Practice",
                type = RecommendationType.QUICK_PRACTICE,
                subject = currentSubject,
                chapter = "Current Syllabus"
            )
        )

        return list
    }

    fun generateRevisionPack(
        classLevel: String,
        subject: String,
        chapter: String
    ): RevisionPack {
        // High quality academic summaries matching official NCERT curriculum
        return when {
            chapter.contains("Chemical", ignoreCase = true) || subject.equals("Chemistry", ignoreCase = true) -> {
                RevisionPack(
                    classLevel = classLevel,
                    subject = subject,
                    chapter = chapter,
                    summary = "Covers balancing chemical reactions, distinguishing combination, decomposition, displacement, and redox mechanisms.",
                    keyConcepts = listOf(
                        "Law of Conservation of Mass: Total mass of reactants equals total mass of products.",
                        "Endothermic reactions absorb thermal energy (e.g. photosynthesis, decomposition of CaCO3).",
                        "Exothermic reactions liberate thermal energy (e.g. cellular respiration, burning of natural gas).",
                        "Oxidation is the gain of oxygen or loss of electrons; Reduction is loss of oxygen or gain of electrons."
                    ),
                    formulas = listOf(
                        "Fe + CuSO4 → FeSO4 + Cu (Displacement)",
                        "2H2O --(electrolysis)--> 2H2 + O2 (Decomposition)",
                        "CH4 + 2O2 → CO2 + 2H2O + Heat (Combustion)"
                    ),
                    definitions = listOf(
                        DefinitionItem("Precipitation Reaction", "A reaction in which an insoluble solid (precipitate) is formed when two solutions mix."),
                        DefinitionItem("Corrosion", "The slow oxidation of metals by air and moisture (e.g. rusting of iron: Fe2O3.xH2O)."),
                        DefinitionItem("Rancidity", "The oxidation of fats and oils in food resulting in foul odor and taste.")
                    ),
                    commonMistakes = listOf(
                        "Forgetting to balance diatomic gases (O2, H2, Cl2).",
                        "Confusing oxidizing agent with substance oxidized (the substance reduced is the oxidizing agent!).",
                        "Omitting physical state symbols (s, l, g, aq) in board examinations."
                    )
                )
            }
            chapter.contains("Light", ignoreCase = true) || subject.equals("Physics", ignoreCase = true) -> {
                RevisionPack(
                    classLevel = classLevel,
                    subject = subject,
                    chapter = chapter,
                    summary = "Geometric optics principles: reflection by spherical mirrors, refraction, Snell's law, and thin lens equations.",
                    keyConcepts = listOf(
                        "Concave mirror converges parallel incident rays to a real focus (f is negative).",
                        "Convex mirror always produces virtual, erect, and diminished images with a wide field of view.",
                        "Refractive index (n) determines bending of light towards or away from the normal."
                    ),
                    formulas = listOf(
                        "Mirror Equation: 1/v + 1/u = 1/f",
                        "Magnification: m = -v/u = h'/h",
                        "Lens Equation: 1/v - 1/u = 1/f",
                        "Power of Lens: P = 1/f (in meters), Unit: Dioptres (D)"
                    ),
                    definitions = listOf(
                        DefinitionItem("Snell's Law", "sin(i) / sin(r) = constant = n2/n1 for a given pair of optical media."),
                        DefinitionItem("Critical Angle", "The angle of incidence in a denser medium for which the angle of refraction in rarer medium is 90°.")
                    ),
                    commonMistakes = listOf(
                        "Incorrect sign conventions: u (object distance) is ALWAYS negative in Cartesian convention.",
                        "Mixing up mirror formula (+) with lens formula (-).",
                        "Entering focal length in cm instead of meters when calculating Lens Power (P = 1/f)."
                    )
                )
            }
            chapter.contains("Life", ignoreCase = true) || subject.equals("Biology", ignoreCase = true) -> {
                RevisionPack(
                    classLevel = classLevel,
                    subject = subject,
                    chapter = chapter,
                    summary = "Vital physiological mechanisms essential for sustaining life: nutrition, cellular respiration, transportation, and excretion.",
                    keyConcepts = listOf(
                        "Autotrophic nutrition relies on chlorophyll, sunlight, CO2, and water to synthesize glucose.",
                        "Aerobic respiration occurs inside mitochondria yielding 36-38 ATP per glucose molecule.",
                        "Double circulation ensures complete separation of oxygenated and deoxygenated blood in humans."
                    ),
                    formulas = listOf(
                        "Photosynthesis: 6CO2 + 6H2O --(light/chlorophyll)--> C6H12O6 + 6O2",
                        "Respiration: C6H12O6 + 6O2 → 6CO2 + 6H2O + Energy (ATP)"
                    ),
                    definitions = listOf(
                        DefinitionItem("Nephron", "The structural and functional filtration unit of the kidney consisting of Bowman's capsule and renal tubules."),
                        DefinitionItem("Translocation", "The transport of soluble products of photosynthesis through phloem sieve tubes utilizing ATP.")
                    ),
                    commonMistakes = listOf(
                        "Confusing xylem (water/unidirectional) with phloem (food/bidirectional).",
                        "Thinking plants only breathe CO2; plants perform respiration 24/7 consuming O2."
                    )
                )
            }
            else -> {
                RevisionPack(
                    classLevel = classLevel,
                    subject = subject,
                    chapter = chapter,
                    summary = "Official NCERT high-yield revision overview for $subject: $chapter.",
                    keyConcepts = listOf(
                        "Master the primary axioms and core theorems tested in standard exams.",
                        "Understand graphical representations, slope definitions, and rate laws.",
                        "Practice standard numerical applications and multi-step derivations."
                    ),
                    formulas = listOf(
                        "Standard General Expression: Y = f(X) + C",
                        "Efficiency Ratio: Output / Input × 100%"
                    ),
                    definitions = listOf(
                        DefinitionItem("Fundamental Law", "Universal physical or mathematical postulate governing standard observations in $subject.")
                    ),
                    commonMistakes = listOf(
                        "Skipping standard SI units in calculation steps.",
                        "Overlooking limiting boundary conditions."
                    )
                )
            }
        }
    }

    fun generateQuickPracticeQuestions(
        classLevel: String,
        subject: String,
        chapter: String,
        storage: AppStorage
    ): List<Question> {
        // Generate a focused 5-question high-impact drill
        return QuestionBank.generateQuestions(
            count = 5,
            optionsCount = 4,
            classLevel = classLevel,
            subject = subject,
            chapter = chapter,
            difficulty = Difficulty.MODERATE
        )
    }

    fun generateMistakeQuiz(mistakes: List<Mistake>): List<Question> {
        return mistakes.mapIndexed { idx, m ->
            Question(
                id = "mistake_q_$idx",
                question = m.question,
                options = m.options,
                correctAnswerIndex = m.correctOptionIndex,
                explanation = "💡 Corrected Concept: ${m.explanation}",
                difficulty = Difficulty.MODERATE,
                classLevel = m.classLevel,
                subject = m.subject,
                chapterName = m.chapter
            )
        }
    }

    data class SearchResults(
        val chats: List<ChatMessage>,
        val flashcards: List<Flashcard>,
        val mistakes: List<Mistake>,
        val revisionPacks: List<RevisionPack>
    ) {
        val totalCount: Int
            get() = chats.size + flashcards.size + mistakes.size + revisionPacks.size
    }

    fun performSmartSearch(query: String, storage: AppStorage): SearchResults {
        val q = query.trim().lowercase()
        if (q.isBlank()) return SearchResults(emptyList(), emptyList(), emptyList(), emptyList())

        val matchedChats = storage.getChatMessages().filter {
            it.text.lowercase().contains(q)
        }

        val matchedCards = storage.getFlashcards().filter {
            it.concept.lowercase().contains(q) || it.explanation.lowercase().contains(q) || it.subject.lowercase().contains(q)
        }

        val matchedMistakes = storage.getMistakes().filter {
            it.question.lowercase().contains(q) || it.explanation.lowercase().contains(q) || it.chapter.lowercase().contains(q)
        }

        val matchedPacks = storage.getRevisionPacks().filter {
            it.chapter.lowercase().contains(q) || it.summary.lowercase().contains(q) || it.subject.lowercase().contains(q)
        }

        return SearchResults(matchedChats, matchedCards, matchedMistakes, matchedPacks)
    }

    // --- Comprehensive Learning Profile & Diagnostics ---

    fun getUserLearningProfile(classLevel: String, storage: AppStorage): UserLearningProfile {
        val user = storage.getUserProfile()
        val allResults = storage.getQuizResults()
        val allMistakes = storage.getMistakes()
        val flashcards = storage.getFlashcards()
        val packs = storage.getRevisionPacks()
        val lastSession = storage.getLastStudiedSession(classLevel)

        val subjectsStudied = allResults.map { it.subject }.distinct()
        val chaptersStudied = allResults.map { it.chapter }.distinct()

        val avgScore = if (allResults.isNotEmpty()) {
            allResults.map { it.percentage }.average().toInt()
        } else {
            0
        }

        val activeMistakes = allMistakes.count { it.status == "Needs review" }
        val masteredMistakes = allMistakes.count { it.status == "Mastered" }

        // Approx study time = ~3 mins per quiz + ~1 min per 3 flashcards + ~4 mins per revision pack
        val approxMinutes = (allResults.size * 3) + (flashcards.size / 3) + (packs.size * 4) + (allMistakes.size * 1)

        val weakAreas = getWeakAreas(classLevel, storage)
        val (recDifficulty, _) = getAdaptiveDifficulty(classLevel, lastSession.subject, storage)

        val overallMastery = if (allResults.isEmpty()) 15 else {
            val scoreWeight = (avgScore * 0.6f).toInt()
            val mistakeWeight = if (allMistakes.isNotEmpty()) {
                ((masteredMistakes.toFloat() / allMistakes.size) * 30f).toInt()
            } else {
                25
            }
            (scoreWeight + mistakeWeight).coerceIn(10, 100)
        }

        return UserLearningProfile(
            classLevel = classLevel,
            subjectsStudied = subjectsStudied,
            chaptersStudied = chaptersStudied,
            totalQuizAttempts = allResults.size,
            averageScore = avgScore,
            totalMistakesRecorded = allMistakes.size,
            activeMistakesCount = activeMistakes,
            mistakesMastered = masteredMistakes,
            flashcardsReviewedCount = flashcards.size,
            revisionPacksCount = packs.size,
            studySessionsCount = allResults.size + packs.size + (flashcards.size / 5),
            approximateStudyMinutes = approxMinutes,
            streak = user.streak,
            xp = user.xp,
            overallMasteryPercent = overallMastery,
            lastOpenedChapter = lastSession.chapter,
            weakAreas = weakAreas,
            recommendedDifficulty = recDifficulty
        )
    }

    fun getSubjectMastery(classLevel: String, subject: String, storage: AppStorage): Int {
        val results = storage.getQuizResults().filter {
            it.classLevel == classLevel && it.subject.equals(subject, ignoreCase = true)
        }
        if (results.isEmpty()) return 20
        return results.map { it.percentage }.average().toInt().coerceIn(10, 100)
    }

    fun getWeakAreas(classLevel: String, storage: AppStorage): List<String> {
        val mistakes = storage.getMistakes().filter { it.status == "Needs review" }
        val frequencyByChapter = mistakes.groupBy { "${it.subject}: ${it.chapter}" }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }

        if (frequencyByChapter.isNotEmpty()) {
            return frequencyByChapter.take(3).map { "${it.first} (${it.second} active mistakes)" }
        }

        // Check lowest quiz averages
        val results = storage.getQuizResults()
        if (results.isNotEmpty()) {
            val lowScoring = results.groupBy { "${it.subject}: ${it.chapter}" }
                .mapValues { it.value.map { r -> r.percentage }.average() }
                .filter { it.value < 70 }
                .toList()
                .sortedBy { it.second }
            if (lowScoring.isNotEmpty()) {
                return lowScoring.take(3).map { "${it.first} (${it.second.toInt()}% avg)" }
            }
        }

        return listOf("All core concepts currently stable. Keep testing to discover weak spots!")
    }

    fun getRecentActivity(storage: AppStorage): List<QuizResult> {
        return storage.getQuizResults().take(8)
    }

    fun getStudyStreak(storage: AppStorage): Int {
        return storage.getUserProfile().streak
    }

    fun getRecommendedAction(classLevel: String, subject: String, storage: AppStorage): SmartRecommendation {
        val recs = getSmartRecommendations(storage, classLevel, subject)
        return recs.firstOrNull() ?: SmartRecommendation(
            title = "Start Daily Practice",
            description = "Complete a quick 5-question check-in to keep your learning momentum.",
            actionLabel = "Practice Now",
            type = RecommendationType.QUICK_PRACTICE,
            subject = subject,
            chapter = "All Chapters"
        )
    }

    fun getAdaptiveDifficulty(classLevel: String, subject: String, storage: AppStorage): Pair<Difficulty, String> {
        val relevantResults = storage.getQuizResults().filter {
            it.subject.equals(subject, ignoreCase = true)
        }

        if (relevantResults.isEmpty()) {
            return Pair(Difficulty.MODERATE, "Starting with balanced standard NCERT difficulty.")
        }

        val recentThree = relevantResults.take(3)
        val avg = recentThree.map { it.percentage }.average().toInt()

        return when {
            avg >= 90 -> Pair(
                Difficulty.HIGH,
                "Outstanding consistency (≥90%). Difficulty increased to Hard mode!"
            )
            avg in 70..89 -> Pair(
                Difficulty.MODERATE,
                "Solid conceptual mastery (${avg}%). Maintaining balanced NCERT standard."
            )
            avg in 40..69 -> Pair(
                Difficulty.MODERATE,
                "Reinforcing core NCERT definitions and mechanics (${avg}%)."
            )
            else -> Pair(
                Difficulty.EASY,
                "Let's strengthen fundamental recall first (${avg}%)."
            )
        }
    }

    fun getWeeklyProgress(storage: AppStorage): WeeklyReport {
        val allResults = storage.getQuizResults()
        val mistakes = storage.getMistakes()
        val profile = storage.getUserProfile()
        val pulse = storage.getDailyPulse()

        val isEligible = allResults.isNotEmpty() || mistakes.isNotEmpty()

        if (!isEligible) {
            return WeeklyReport(
                totalSessions = 0,
                studyTimeMinutes = 0,
                quizzesCompleted = 0,
                averageScore = 0,
                strongestSubject = "N/A",
                weakestSubject = "N/A",
                strongestChapter = "N/A",
                weakestChapter = "N/A",
                mistakesImproved = 0,
                masteryGainedPercent = 0,
                xpEarned = 0,
                streak = profile.streak,
                insight = "Not enough activity yet. Keep studying and your report will build automatically.",
                isEligible = false
            )
        }

        val subjectGroups = allResults.groupBy { it.subject }
        val strongestSub = subjectGroups.maxByOrNull { it.value.map { r -> r.percentage }.average() }?.key ?: "Science"
        val weakestSub = subjectGroups.minByOrNull { it.value.map { r -> r.percentage }.average() }?.key ?: strongestSub

        val chapterGroups = allResults.groupBy { it.chapter }
        val strongestChap = chapterGroups.maxByOrNull { it.value.map { r -> r.percentage }.average() }?.key ?: "Fundamental Units"
        val weakestChap = chapterGroups.minByOrNull { it.value.map { r -> r.percentage }.average() }?.key ?: strongestChap

        val avgScore = allResults.map { it.percentage }.average().toInt()
        val totalSessions = allResults.size + (mistakes.size / 2) + 1
        val studyMinutes = (allResults.size * 3) + (mistakes.size * 2) + 5
        val improvedMistakes = mistakes.count { it.status == "Improving" || it.status == "Mastered" }
        val xpEarned = allResults.sumOf { it.xpEarned } + pulse.xpToday

        val insight = when {
            avgScore >= 85 -> "Outstanding week! You demonstrated top-tier conceptual accuracy in $strongestSub. Maintain your streak with advanced Olympiad or Hard-mode challenges."
            avgScore >= 70 -> "Great steady progress! Your performance in $strongestChap was strong. Focus your revision on $weakestChap to eliminate residual errors."
            else -> "Active learning week! You tackled $totalSessions study sessions. Prioritize the Mistake Bank drills to solidify foundational concepts in $weakestSub."
        }

        return WeeklyReport(
            totalSessions = totalSessions,
            studyTimeMinutes = studyMinutes,
            quizzesCompleted = allResults.size,
            averageScore = avgScore,
            strongestSubject = strongestSub,
            weakestSubject = weakestSub,
            strongestChapter = strongestChap,
            weakestChapter = weakestChap,
            mistakesImproved = improvedMistakes,
            masteryGainedPercent = (avgScore * 0.35f + improvedMistakes * 5).toInt().coerceIn(5, 45),
            xpEarned = xpEarned,
            streak = profile.streak,
            insight = insight,
            isEligible = true
        )
    }

    fun getConceptMasteryMap(classLevel: String, subject: String, storage: AppStorage): List<ConceptMasteryItem> {
        val chapters = CurriculumData.getChapters(classLevel, subject)
        val allMistakes = storage.getMistakes()
        val allResults = storage.getQuizResults()

        val list = mutableListOf<ConceptMasteryItem>()

        chapters.forEach { chap ->
            val chapResults = allResults.filter {
                it.subject.equals(subject, ignoreCase = true) &&
                (it.chapter.contains(chap.name, ignoreCase = true) || chap.name.contains(it.chapter, ignoreCase = true))
            }
            val chapMistakes = allMistakes.filter {
                it.subject.equals(subject, ignoreCase = true) &&
                (it.chapter.contains(chap.name, ignoreCase = true) || chap.name.contains(it.chapter, ignoreCase = true))
            }

            val avg = if (chapResults.isNotEmpty()) chapResults.map { it.percentage }.average().toInt() else 0
            val activeMis = chapMistakes.count { it.status == "Needs review" }
            val concepts = chap.keyConcepts.ifEmpty {
                listOf("${chap.name} Fundamentals", "Key Principles & Laws", "Formula & Numerical Analysis", "Board Exam Application")
            }

            concepts.forEachIndexed { idx, conceptName ->
                val mastery = when {
                    chapResults.isEmpty() && chapMistakes.isEmpty() -> 10 + (idx * 5)
                    activeMis > 0 -> (avg * 0.7f).toInt().coerceIn(20, 65)
                    avg >= 85 -> (avg - (idx * 2)).coerceIn(80, 100)
                    avg >= 60 -> (avg + 5 - (idx * 3)).coerceIn(50, 85)
                    else -> 35
                }

                val state = when {
                    chapResults.isEmpty() && chapMistakes.isEmpty() -> ConceptMasteryState.NOT_STARTED
                    mastery >= 85 -> ConceptMasteryState.MASTERED
                    mastery >= 70 -> ConceptMasteryState.STRONG
                    mastery >= 50 -> ConceptMasteryState.DEVELOPING
                    else -> ConceptMasteryState.LEARNING
                }

                list.add(
                    ConceptMasteryItem(
                        conceptName = conceptName,
                        chapterName = chap.name,
                        subjectName = subject,
                        classLevel = classLevel,
                        masteryPercent = mastery,
                        state = state,
                        mistakesCount = activeMis,
                        quizzesCount = chapResults.size
                    )
                )
            }
        }

        return list
    }

    fun generatePersonalizedChallenges(
        classLevel: String,
        subject: String,
        chapter: String,
        storage: AppStorage
    ): List<ChallengeConfig> {
        val mistakes = storage.getMistakes().filter { it.status == "Needs review" }
        val (adaptiveDiff, _) = getAdaptiveDifficulty(classLevel, subject, storage)

        return listOf(
            ChallengeConfig(
                type = ChallengeType.WEAK_AREA,
                title = "Weak Area Elimination",
                subtitle = "Target concepts that need reinforcement",
                description = "Focused 5-question targeted drill on chapters with active mistakes.",
                subject = subject,
                chapter = chapter,
                questionCount = 5,
                timerSecondsPerQuestion = 30,
                difficulty = adaptiveDiff,
                xpReward = 60,
                classLevel = classLevel,
                isExamMode = false
            ),
            ChallengeConfig(
                type = ChallengeType.SPEED,
                title = "Rapid Speed Challenge",
                subtitle = "15 seconds per question",
                description = "High-velocity round testing instant NCERT conceptual recall and formula speed.",
                subject = subject,
                chapter = chapter,
                questionCount = 5,
                timerSecondsPerQuestion = 15,
                difficulty = Difficulty.MODERATE,
                xpReward = 75,
                classLevel = classLevel,
                isExamMode = false
            ),
            ChallengeConfig(
                type = ChallengeType.PERFECT_SCORE,
                title = "Flawless 100% Challenge",
                subtitle = "Zero room for errors",
                description = "Complete 5 standard exam questions with 100% accuracy to earn massive bonus XP.",
                subject = subject,
                chapter = chapter,
                questionCount = 5,
                timerSecondsPerQuestion = 30,
                difficulty = Difficulty.MODERATE,
                xpReward = 100,
                classLevel = classLevel,
                isExamMode = true
            ),
            ChallengeConfig(
                type = ChallengeType.HARD_MODE,
                title = "Olympiad & Hard Mode",
                subtitle = "High-order thinking questions",
                description = "Tackle multi-step application and Assertion-Reason traps designed for top percentiles.",
                subject = subject,
                chapter = chapter,
                questionCount = 5,
                timerSecondsPerQuestion = 45,
                difficulty = Difficulty.HIGH,
                xpReward = 90,
                classLevel = classLevel,
                isExamMode = true
            ),
            ChallengeConfig(
                type = ChallengeType.MISTAKE,
                title = "Mistake Bank Drill",
                subtitle = "${mistakes.size} questions pending review",
                description = "Retest questions you previously got wrong until you achieve full mastery.",
                subject = subject,
                chapter = "Mistake Bank Drill",
                questionCount = mistakes.size.coerceIn(1, 10),
                timerSecondsPerQuestion = 30,
                difficulty = Difficulty.MODERATE,
                xpReward = 50,
                classLevel = classLevel,
                isExamMode = false
            ),
            ChallengeConfig(
                type = ChallengeType.MIXED_SUBJECT,
                title = "Multi-Disciplinary Sprint",
                subtitle = "Cross-subject comprehensive test",
                description = "Full syllabus mixed sprint testing agility across all your core NCERT subjects.",
                subject = subject,
                chapter = "All Chapters (Full Syllabus)",
                questionCount = 10,
                timerSecondsPerQuestion = 30,
                difficulty = Difficulty.MODERATE,
                xpReward = 85,
                classLevel = classLevel,
                isExamMode = false
            )
        )
    }

    // --- ThinkX Command Center Interpreter ---

    sealed class CommandAction {
        data class StartQuiz(
            val classLevel: String,
            val subject: String,
            val chapter: String,
            val count: Int,
            val difficulty: Difficulty,
            val isExamMode: Boolean = false
        ) : CommandAction()
        data class OpenRevise(val subject: String, val chapter: String) : CommandAction()
        data class OpenMistakes(val subject: String) : CommandAction()
        data class AskAI(val prompt: String, val subject: String, val chapter: String) : CommandAction()
        data class OpenMastery(val subject: String = "") : CommandAction()
        data class OpenChallenge(val challengeType: ChallengeType = ChallengeType.SPEED) : CommandAction()
        data class OpenWeeklyReport(val insight: String = "") : CommandAction()
        data class OpenFlashcards(val subject: String = "") : CommandAction()
        data class Unknown(val rawQuery: String) : CommandAction()
    }

    fun interpretCommand(
        query: String,
        classLevel: String,
        currentSubject: String,
        currentChapter: String,
        storage: AppStorage
    ): CommandAction {
        val q = query.trim().lowercase()

        // 1. Mistakes
        if (q.contains("mistake") || q.contains("wrong answer") || q.contains("errors")) {
            return CommandAction.OpenMistakes(currentSubject)
        }

        // 2. Weekly Report / Stats / Pulse
        if (q.contains("week") || q.contains("report") || q.contains("my progress") || q.contains("summary of this week")) {
            val report = getWeeklyProgress(storage)
            return CommandAction.OpenWeeklyReport(report.insight)
        }

        // 3. Concept Mastery / Concept Map
        if (q.contains("mastery") || q.contains("concept map") || q.contains("breakdown") || q.contains("weak chapters") || q.contains("weakest")) {
            return CommandAction.OpenMastery(currentSubject)
        }

        // 4. Challenge Me
        if (q.contains("challenge") || q.contains("speed quiz") || q.contains("beat my score") || q.contains("hard mode")) {
            val type = when {
                q.contains("speed") -> ChallengeType.SPEED
                q.contains("hard") || q.contains("olympiad") -> ChallengeType.HARD_MODE
                q.contains("perfect") -> ChallengeType.PERFECT_SCORE
                q.contains("weak") -> ChallengeType.WEAK_AREA
                else -> ChallengeType.SPEED
            }
            return CommandAction.OpenChallenge(type)
        }

        // 5. Revision
        if (q.contains("revise") || q.contains("notes") || q.contains("formula") || q.contains("summary")) {
            // Check if a specific subject or chapter is mentioned
            val subjects = CurriculumData.getSubjects(classLevel)
            val matchedSub = subjects.firstOrNull { q.contains(it.lowercase()) } ?: currentSubject
            val matchedChap = CurriculumData.getChapters(classLevel, matchedSub).firstOrNull {
                q.contains(it.name.lowercase())
            }?.name ?: currentChapter
            return CommandAction.OpenRevise(matchedSub, matchedChap)
        }

        // 5.1 Flashcards
        if (q.contains("flashcard") || q.contains("card") || q.contains("deck")) {
            return CommandAction.OpenFlashcards(currentSubject)
        }

        // 6. Direct AI Query / Explanation
        if (q.startsWith("explain") || q.startsWith("what is") || q.startsWith("why") || q.startsWith("how does") || q.contains("tell me about")) {
            return CommandAction.AskAI(query, currentSubject, currentChapter)
        }

        // 7. Start Quiz / Test
        if (q.contains("quiz") || q.contains("test") || q.contains("practice") || q.contains("exam")) {
            val isExam = q.contains("exam")
            val isHard = q.contains("hard") || q.contains("difficult")
            val subjects = CurriculumData.getSubjects(classLevel)
            val matchedSub = subjects.firstOrNull { q.contains(it.lowercase()) } ?: currentSubject
            val matchedChap = CurriculumData.getChapters(classLevel, matchedSub).firstOrNull {
                q.contains(it.name.lowercase())
            }?.name ?: currentChapter

            return CommandAction.StartQuiz(
                classLevel = classLevel,
                subject = matchedSub,
                chapter = matchedChap,
                count = 5,
                difficulty = if (isHard) Difficulty.HIGH else Difficulty.MODERATE,
                isExamMode = isExam
            )
        }

        // Default to AI query
        return CommandAction.AskAI(query, currentSubject, currentChapter)
    }
}
