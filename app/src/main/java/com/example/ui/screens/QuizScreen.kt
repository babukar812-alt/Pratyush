package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStorage
import com.example.model.Question
import com.example.model.QuizConfig
import com.example.model.QuizResult
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    config: QuizConfig,
    questions: List<Question>,
    onQuizCancelled: () -> Unit,
    onQuizFinished: (QuizResult) -> Unit
) {
    val palette = LocalThemePalette.current
    val context = LocalContext.current
    val storage = remember { AppStorage(context) }
    val scrollState = rememberScrollState()

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    // Statistics tracking
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var incorrectAnswersCount by remember { mutableIntStateOf(0) }
    var skippedCount by remember { mutableIntStateOf(0) }
    var totalTimeSpentSeconds by remember { mutableIntStateOf(0) }

    val currentQuestion = questions.getOrNull(currentIndex)

    // Per-question timer countdown
    var timeLeft by remember(currentIndex) { mutableIntStateOf(config.timerSecondsPerQuestion) }
    var isTimerActive by remember { mutableStateOf(true) }

    // Timer effect with cleanup
    LaunchedEffect(currentIndex, isAnswerSubmitted, isTimerActive) {
        if (!isAnswerSubmitted && isTimerActive && timeLeft > 0) {
            while (timeLeft > 0 && !isAnswerSubmitted && isTimerActive) {
                delay(1000)
                timeLeft -= 1
                totalTimeSpentSeconds += 1
            }
            if (timeLeft <= 0 && !isAnswerSubmitted) {
                // Automatic timeout: count as skipped and reveal explanation
                skippedCount += 1
                isAnswerSubmitted = true
                currentQuestion?.let { q ->
                    storage.recordMistake(q, -1, config.classLevel, config.subject, config.chapter)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            isTimerActive = false
        }
    }

    fun moveToNextQuestion() {
        if (currentIndex < questions.size - 1) {
            currentIndex += 1
            selectedOptionIndex = null
            isAnswerSubmitted = false
            timeLeft = config.timerSecondsPerQuestion
        } else {
            // Quiz Complete
            isTimerActive = false
            val score = correctAnswersCount * 10
            val percentage = if (questions.isNotEmpty()) (correctAnswersCount * 100) / questions.size else 0
            val xpEarned = (correctAnswersCount * 10) + 20 // 20 XP for completion + 10 XP per correct

            val result = QuizResult(
                score = score,
                totalQuestions = questions.size,
                percentage = percentage,
                correctCount = correctAnswersCount,
                incorrectCount = incorrectAnswersCount,
                skippedCount = skippedCount,
                timeSpentSeconds = totalTimeSpentSeconds,
                subject = config.subject,
                chapter = config.chapter,
                difficulty = config.difficulty.label,
                xpEarned = xpEarned
            )
            storage.recordDailyQuizActivity(xpEarned, questions.size, correctAnswersCount)
            storage.saveLastStudiedSession(config.classLevel, config.subject, config.chapter)
            onQuizFinished(result)
        }
    }

    if (currentQuestion == null) {
        // Fallback safety
        LiquidBackground {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                GlassButton(text = "Return to Dashboard", onClick = onQuizCancelled)
            }
        }
        return
    }

    LiquidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Quiz Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onQuizCancelled,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.borderGlass, CircleShape)
                        .testTag("quiz_exit_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Exit Quiz",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "QUESTION ${currentIndex + 1} / ${questions.size}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = palette.primaryAccent,
                    letterSpacing = 1.5.sp
                )

                // Timer Pill
                val timerFraction = timeLeft.toFloat() / config.timerSecondsPerQuestion
                val timerColor = if (timeLeft <= 5) SoftRed else palette.primaryAccent

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(palette.surfaceGlass)
                        .border(1.dp, timerColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        tint = timerColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${timeLeft}s",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = timerColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            val progressFraction = (currentIndex + 1).toFloat() / questions.size
            GlassProgress(progress = progressFraction, height = 6.dp)

            Spacer(modifier = Modifier.height(18.dp))

            // Question Box
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlow = true,
                cornerRadius = 20.dp,
                contentPadding = PaddingValues(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassBadge(
                        text = if (currentQuestion.isAssertionReason) "ASSERTION-REASON" else currentQuestion.subject.uppercase(),
                        color = if (currentQuestion.isAssertionReason) palette.secondaryAccent else palette.primaryAccent
                    )
                    Text(
                        text = currentQuestion.difficulty.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentQuestion.question,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQuestion.options.forEachIndexed { index, optionText ->
                    val isSelected = selectedOptionIndex == index
                    val isCorrect = index == currentQuestion.correctAnswerIndex

                    // Animated appearance
                    val cardBorderColor by animateColorAsState(
                        targetValue = when {
                            isAnswerSubmitted && isCorrect -> SoftGreen
                            isAnswerSubmitted && isSelected && !isCorrect -> SoftRed
                            isSelected -> palette.primaryAccent
                            else -> palette.borderGlass
                        },
                        animationSpec = tween(200),
                        label = "optionBorder"
                    )

                    val cardBgColor by animateColorAsState(
                        targetValue = when {
                            isAnswerSubmitted && isCorrect -> SoftGreen.copy(alpha = 0.2f)
                            isAnswerSubmitted && isSelected && !isCorrect -> SoftRed.copy(alpha = 0.2f)
                            isSelected -> palette.primaryAccent.copy(alpha = 0.15f)
                            else -> palette.surfaceGlass.copy(alpha = 0.6f)
                        },
                        animationSpec = tween(200),
                        label = "optionBg"
                    )

                    val optionLetter = ('A' + index).toString()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBgColor)
                            .border(
                                width = if (isSelected || (isAnswerSubmitted && isCorrect)) 1.8.dp else 1.dp,
                                color = cardBorderColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable(enabled = !isAnswerSubmitted) {
                                if (!isAnswerSubmitted) {
                                    selectedOptionIndex = index
                                    isAnswerSubmitted = true
                                    if (isCorrect) {
                                        correctAnswersCount += 1
                                        storage.recordMistakeCorrected(currentQuestion.question)
                                    } else {
                                        incorrectAnswersCount += 1
                                        storage.recordMistake(currentQuestion, index, config.classLevel, config.subject, config.chapter)
                                    }
                                }
                            }
                            .testTag("quiz_option_$index")
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isAnswerSubmitted && isCorrect -> SoftGreen
                                            isAnswerSubmitted && isSelected && !isCorrect -> SoftRed
                                            isSelected -> palette.primaryAccent
                                            else -> palette.surfaceGlass
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetter,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected || (isAnswerSubmitted && isCorrect)) VoidBlack else TextWhite
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = optionText,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = TextWhite,
                                modifier = Modifier.weight(1f)
                            )

                            if (isAnswerSubmitted) {
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = SoftGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Rounded.Cancel,
                                        contentDescription = "Incorrect",
                                        tint = SoftRed,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Explanation Card (revealed after submission)
            AnimatedVisibility(
                visible = isAnswerSubmitted,
                enter = fadeIn() + slideInVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 16.dp,
                        borderGlow = false,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Lightbulb,
                                contentDescription = null,
                                tint = AmberGlow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "EXPLANATION & CONCEPT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGlow,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQuestion.explanation,
                            fontSize = 13.sp,
                            color = TextWhite,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons Row (Skip / Next Question)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isAnswerSubmitted) {
                    GlassButton(
                        text = "SKIP",
                        onClick = {
                            skippedCount += 1
                            isAnswerSubmitted = true
                        },
                        isSecondary = true,
                        modifier = Modifier.weight(1f),
                        testTag = "quiz_skip_button"
                    )
                }

                if (isAnswerSubmitted) {
                    GlassButton(
                        text = if (currentIndex < questions.size - 1) "NEXT QUESTION" else "SUBMIT ROUND",
                        onClick = { moveToNextQuestion() },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = if (currentIndex < questions.size - 1) Icons.Rounded.ArrowForward else Icons.Rounded.Check,
                                contentDescription = null,
                                tint = VoidBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        testTag = "quiz_next_button"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
