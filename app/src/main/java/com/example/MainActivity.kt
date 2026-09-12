package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ai.ThinkXAIService
import com.example.data.AppStorage
import com.example.data.CurriculumData
import com.example.data.NCERTData
import com.example.data.QuestionBank
import com.example.data.ThinkXIntelligence
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ThinkXTheme
import kotlinx.coroutines.launch

enum class AppStage {
    SPLASH,
    AUTH,
    MAIN,
    QUIZ_CONFIG,
    QUIZ_LOADING,
    QUIZ_ACTIVE,
    QUIZ_RESULT
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThinkXApp()
        }
    }
}

@Composable
fun ThinkXApp() {
    val context = LocalContext.current
    val storage = remember { AppStorage(context) }
    val coroutineScope = rememberCoroutineScope()

    var userProfile by remember { mutableStateOf(storage.getUserProfile()) }
    var currentStage by remember {
        mutableStateOf(AppStage.SPLASH)
    }
    var currentDestination by remember { mutableStateOf(AppDestination.HOME) }

    var selectedClass by remember { mutableStateOf(userProfile.classLevel) }
    var selectedSubject by remember { mutableStateOf("Science") }
    var selectedChapter by remember { mutableStateOf("All Chapters (Full Syllabus)") }

    var quizConfig by remember { mutableStateOf(storage.getQuizConfig()) }
    var activeQuizQuestions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentQuizResult by remember { mutableStateOf<QuizResult?>(null) }

    var flashcards by remember { mutableStateOf(storage.getFlashcards()) }
    var chatMessages by remember { mutableStateOf(storage.getChatMessages()) }

    var showVoiceDialog by remember { mutableStateOf(false) }
    var showImageGenDialog by remember { mutableStateOf(false) }

    // Update streak on launch
    LaunchedEffect(Unit) {
        val updatedStreak = storage.updateStreak()
        userProfile = userProfile.copy(streak = updatedStreak)
    }

    // Back button handling
    BackHandler(enabled = currentStage != AppStage.MAIN && currentStage != AppStage.SPLASH) {
        when (currentStage) {
            AppStage.QUIZ_CONFIG -> currentStage = AppStage.MAIN
            AppStage.QUIZ_LOADING -> currentStage = AppStage.QUIZ_CONFIG
            AppStage.QUIZ_ACTIVE -> currentStage = AppStage.MAIN
            AppStage.QUIZ_RESULT -> currentStage = AppStage.MAIN
            AppStage.AUTH -> {
                // If already logged in previously, allow back to main
                if (storage.isUserLoggedIn()) currentStage = AppStage.MAIN
            }
            else -> {}
        }
    }

    ThinkXTheme(selectedTheme = userProfile.theme) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentStage) {
                AppStage.SPLASH -> {
                    SplashScreen(
                        onSplashFinished = {
                            if (storage.isUserLoggedIn()) {
                                currentStage = AppStage.MAIN
                            } else {
                                currentStage = AppStage.AUTH
                            }
                        }
                    )
                }

                AppStage.AUTH -> {
                    AuthScreen(
                        onAuthenticated = { newProfile ->
                            userProfile = newProfile
                            selectedClass = newProfile.classLevel
                            if (!CurriculumData.isSubjectValidForClass(newProfile.classLevel, selectedSubject)) {
                                selectedSubject = CurriculumData.getDefaultSubject(newProfile.classLevel)
                            }
                            val available = CurriculumData.getChapterNames(newProfile.classLevel, selectedSubject, includeAll = true)
                            selectedChapter = available.firstOrNull() ?: "All Chapters (Full Syllabus)"
                            storage.saveUserProfile(newProfile)
                            storage.setLoggedIn(true)
                            currentStage = AppStage.MAIN
                        }
                    )
                }

                AppStage.QUIZ_CONFIG -> {
                    QuizConfigScreen(
                        initialConfig = quizConfig.copy(
                            classLevel = selectedClass,
                            subject = selectedSubject,
                            chapter = selectedChapter
                        ),
                        onBack = { currentStage = AppStage.MAIN },
                        onStartQuiz = { configured ->
                            quizConfig = configured
                            storage.saveQuizConfig(configured)
                            activeQuizQuestions = QuestionBank.generateQuestions(
                                count = configured.questionCount,
                                optionsCount = configured.optionsCount,
                                classLevel = configured.classLevel,
                                subject = configured.subject,
                                chapter = configured.chapter,
                                difficulty = configured.difficulty
                            )
                            currentStage = AppStage.QUIZ_LOADING
                        }
                    )
                }

                AppStage.QUIZ_LOADING -> {
                    ThinkXLoader(
                        isQuizMode = true,
                        customTitle = "Generating NCERT Quiz",
                        fullscreen = true,
                        onComplete = {
                            currentStage = AppStage.QUIZ_ACTIVE
                        }
                    )
                }

                AppStage.QUIZ_ACTIVE -> {
                    QuizScreen(
                        config = quizConfig,
                        questions = activeQuizQuestions,
                        onQuizCancelled = { currentStage = AppStage.MAIN },
                        onQuizFinished = { result ->
                            currentQuizResult = result
                            storage.saveQuizResult(result)

                            // Update user career stats
                            val newProfile = userProfile.copy(
                                xp = userProfile.xp + result.xpEarned,
                                totalQuizzes = userProfile.totalQuizzes + 1,
                                totalQuestionsAnswered = userProfile.totalQuestionsAnswered + result.totalQuestions,
                                totalCorrect = userProfile.totalCorrect + result.correctCount
                            )
                            userProfile = newProfile
                            storage.saveUserProfile(newProfile)

                            currentStage = AppStage.QUIZ_RESULT
                        }
                    )
                }

                AppStage.QUIZ_RESULT -> {
                    val res = currentQuizResult
                    if (res != null) {
                        QuizResultScreen(
                            result = res,
                            onPlayAgain = {
                                activeQuizQuestions = QuestionBank.generateQuestions(
                                    count = quizConfig.questionCount,
                                    optionsCount = quizConfig.optionsCount,
                                    classLevel = quizConfig.classLevel,
                                    subject = quizConfig.subject,
                                    chapter = quizConfig.chapter,
                                    difficulty = quizConfig.difficulty
                                )
                                currentStage = AppStage.QUIZ_LOADING
                            },
                            onBackToDashboard = {
                                currentDestination = AppDestination.HOME
                                currentStage = AppStage.MAIN
                            },
                            onDiscussWithAI = {
                                currentDestination = AppDestination.AI
                                currentStage = AppStage.MAIN
                                // Prepend AI doubt discussion
                                val promptText = "I just scored ${res.percentage}% (${res.correctCount}/${res.totalQuestions}) on the ${res.difficulty} test for ${res.subject} (${res.chapter}). Please analyze where students typically make mistakes in this chapter and help me master it."
                                coroutineScope.launch {
                                    val updatedMessages = chatMessages.toMutableList()
                                    val userMsg = ChatMessage(sender = MessageSender.USER, text = promptText)
                                    updatedMessages.add(userMsg)
                                    chatMessages = updatedMessages
                                    storage.saveChatMessages(updatedMessages)

                                    val aiReply = ThinkXAIService.getAIResponse(
                                        userPrompt = promptText,
                                        classLevel = selectedClass,
                                        subject = selectedSubject,
                                        chapter = selectedChapter
                                    )
                                    val aiMsg = ChatMessage(sender = MessageSender.AI, text = aiReply)
                                    val finalMessages = updatedMessages + aiMsg
                                    chatMessages = finalMessages
                                    storage.saveChatMessages(finalMessages)
                                }
                            },
                            onPracticeMistakes = {
                                val mistakes = storage.getMistakes().filter { it.status == "Needs review" }
                                if (mistakes.isNotEmpty()) {
                                    val questions = ThinkXIntelligence.generateMistakeQuiz(mistakes)
                                    activeQuizQuestions = questions
                                    quizConfig = quizConfig.copy(
                                        classLevel = selectedClass,
                                        subject = res.subject,
                                        chapter = "Mistake Bank Drill",
                                        questionCount = questions.size,
                                        difficulty = Difficulty.MODERATE
                                    )
                                    currentStage = AppStage.QUIZ_LOADING
                                }
                            }
                        )
                    } else {
                        currentStage = AppStage.MAIN
                    }
                }

                AppStage.MAIN -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (currentDestination) {
                            AppDestination.HOME -> {
                                HomeScreen(
                                    userProfile = userProfile,
                                    selectedClass = selectedClass,
                                    onClassSelected = { cls ->
                                        selectedClass = cls
                                        if (!CurriculumData.isSubjectValidForClass(cls, selectedSubject)) {
                                            selectedSubject = CurriculumData.getDefaultSubject(cls)
                                        }
                                        val available = CurriculumData.getChapterNames(cls, selectedSubject, includeAll = true)
                                        selectedChapter = available.firstOrNull() ?: "All Chapters (Full Syllabus)"
                                        userProfile = userProfile.copy(classLevel = cls)
                                        storage.saveUserProfile(userProfile)
                                    },
                                    selectedSubject = selectedSubject,
                                    onSubjectSelected = { sub ->
                                        selectedSubject = sub
                                        val available = CurriculumData.getChapterNames(selectedClass, sub, includeAll = true)
                                        selectedChapter = available.firstOrNull() ?: "All Chapters (Full Syllabus)"
                                    },
                                    selectedChapter = selectedChapter,
                                    onChapterSelected = { ch ->
                                        selectedChapter = ch
                                    },
                                    onLaunchConfigurator = {
                                        currentStage = AppStage.QUIZ_CONFIG
                                    },
                                    onOpenProfile = {
                                        currentDestination = AppDestination.PROFILE
                                    },
                                    onStartQuickQuiz = { cls, sub, ch ->
                                        selectedClass = cls
                                        selectedSubject = sub
                                        selectedChapter = ch
                                        val questions = QuestionBank.generateQuestions(
                                            count = 5,
                                            optionsCount = 4,
                                            classLevel = cls,
                                            subject = sub,
                                            chapter = ch,
                                            difficulty = Difficulty.MODERATE
                                        )
                                        activeQuizQuestions = questions
                                        quizConfig = quizConfig.copy(
                                            classLevel = cls,
                                            subject = sub,
                                            chapter = ch,
                                            questionCount = 5,
                                            difficulty = Difficulty.MODERATE
                                        )
                                        currentStage = AppStage.QUIZ_LOADING
                                    },
                                    onNavigateToMistakes = {
                                        currentDestination = AppDestination.REVISE
                                    },
                                    onNavigateToAI = { cls, sub, ch ->
                                        selectedClass = cls
                                        selectedSubject = sub
                                        selectedChapter = ch
                                        currentDestination = AppDestination.AI
                                    },
                                    onNavigateToRevise = {
                                        currentDestination = AppDestination.REVISE
                                    },
                                    onNavigateToCards = {
                                        currentDestination = AppDestination.CARDS
                                    },
                                    onStartChallenge = { challenge ->
                                        selectedClass = challenge.classLevel
                                        selectedSubject = challenge.subject
                                        selectedChapter = challenge.chapter
                                        val questions = QuestionBank.generateQuestions(
                                            count = challenge.questionCount,
                                            optionsCount = 4,
                                            classLevel = challenge.classLevel,
                                            subject = challenge.subject,
                                            chapter = challenge.chapter,
                                            difficulty = challenge.difficulty
                                        )
                                        activeQuizQuestions = questions
                                        quizConfig = quizConfig.copy(
                                            classLevel = challenge.classLevel,
                                            subject = challenge.subject,
                                            chapter = challenge.chapter,
                                            questionCount = challenge.questionCount,
                                            optionsCount = 4,
                                            timerSecondsPerQuestion = challenge.timerSecondsPerQuestion,
                                            difficulty = challenge.difficulty,
                                            isExamMode = challenge.isExamMode
                                        )
                                        currentStage = AppStage.QUIZ_LOADING
                                    }
                                )
                            }

                            AppDestination.REVISE -> {
                                ReviseScreen(
                                    userProfile = userProfile,
                                    onStartRevisionQuiz = { sub, ch ->
                                        selectedSubject = sub
                                        selectedChapter = ch
                                        quizConfig = quizConfig.copy(
                                            classLevel = selectedClass,
                                            subject = sub,
                                            chapter = ch,
                                            questionCount = 10,
                                            difficulty = Difficulty.MODERATE
                                        )
                                        activeQuizQuestions = QuestionBank.generateQuestions(
                                            count = 10,
                                            optionsCount = 4,
                                            classLevel = selectedClass,
                                            subject = sub,
                                            chapter = ch,
                                            difficulty = Difficulty.MODERATE
                                        )
                                        currentStage = AppStage.QUIZ_LOADING
                                    },
                                    onPracticeMistakes = { mistakeQuestions ->
                                        activeQuizQuestions = mistakeQuestions
                                        quizConfig = quizConfig.copy(
                                            classLevel = selectedClass,
                                            subject = selectedSubject,
                                            chapter = "Mistake Bank Drill",
                                            questionCount = mistakeQuestions.size,
                                            difficulty = Difficulty.MODERATE
                                        )
                                        currentStage = AppStage.QUIZ_LOADING
                                    },
                                    onSaveCard = { card ->
                                        storage.addCustomFlashcard(card)
                                        flashcards = storage.getFlashcards()
                                    }
                                )
                            }

                            AppDestination.CARDS -> {
                                FlashcardsScreen(
                                    flashcards = flashcards,
                                    onAddCustomCard = { card ->
                                        storage.addCustomFlashcard(card)
                                        flashcards = storage.getFlashcards()
                                    }
                                )
                            }

                            AppDestination.AI -> {
                                AIScreen(
                                    chatMessages = chatMessages,
                                    onSendMessage = { userText ->
                                        val updated = chatMessages.toMutableList()
                                        val userMsg = ChatMessage(sender = MessageSender.USER, text = userText)
                                        updated.add(userMsg)
                                        chatMessages = updated
                                        storage.saveChatMessages(updated)

                                        coroutineScope.launch {
                                            val aiText = ThinkXAIService.getAIResponse(
                                                userPrompt = userText,
                                                classLevel = selectedClass,
                                                subject = selectedSubject,
                                                chapter = selectedChapter
                                            )
                                            val aiMsg = ChatMessage(sender = MessageSender.AI, text = aiText)
                                            val withAi = updated + aiMsg
                                            chatMessages = withAi
                                            storage.saveChatMessages(withAi)
                                        }
                                    },
                                    onClearChat = {
                                        storage.clearChat()
                                        chatMessages = storage.getChatMessages()
                                    },
                                    onOpenVoice = { showVoiceDialog = true },
                                    onOpenImageGen = { showImageGenDialog = true },
                                    currentClass = selectedClass,
                                    currentSubject = selectedSubject,
                                    currentChapter = selectedChapter
                                )
                            }

                            AppDestination.BADGES -> {
                                BadgesScreen(userProfile = userProfile)
                            }

                            AppDestination.PROFILE -> {
                                ProfileScreen(
                                    userProfile = userProfile,
                                    onThemeSelected = { newTheme ->
                                        val updated = userProfile.copy(theme = newTheme)
                                        userProfile = updated
                                        storage.saveUserProfile(updated)
                                    },
                                    onLogout = {
                                        storage.setLoggedIn(false)
                                        currentStage = AppStage.AUTH
                                    }
                                )
                            }
                        }

                        // Floating Glass Bottom Navigation Dock
                        GlassBottomDock(
                            currentDestination = currentDestination,
                            onDestinationSelected = { dest -> currentDestination = dest },
                            modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                        )
                    }
                }
            }

            // Global Dialogs
            if (showVoiceDialog) {
                VoiceAiDialog(
                    onDismiss = { showVoiceDialog = false },
                    classLevel = selectedClass,
                    currentSubject = selectedSubject,
                    currentChapter = selectedChapter,
                    onTranscriptionReceived = { text ->
                        showVoiceDialog = false
                        currentDestination = AppDestination.AI
                    }
                )
            }

            if (showImageGenDialog) {
                ImageGenDialog(
                    onDismiss = { showImageGenDialog = false },
                    currentChapter = selectedChapter
                )
            }
        }
    }
}

// Preserve Greeting for unit and screenshot testing
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}
