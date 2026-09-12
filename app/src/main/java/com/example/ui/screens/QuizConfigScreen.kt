package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.model.QuizConfig
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun QuizConfigScreen(
    initialConfig: QuizConfig,
    onBack: () -> Unit,
    onStartQuiz: (QuizConfig) -> Unit
) {
    val palette = LocalThemePalette.current
    val scrollState = rememberScrollState()

    var questionCount by remember { mutableIntStateOf(initialConfig.questionCount) }
    var optionsCount by remember { mutableIntStateOf(initialConfig.optionsCount) }
    var timerSeconds by remember { mutableIntStateOf(initialConfig.timerSecondsPerQuestion) }
    var difficulty by remember { mutableStateOf(initialConfig.difficulty) }

    val liveSummary = remember(questionCount, optionsCount, timerSeconds, difficulty) {
        "${questionCount}Q • ${optionsCount}opt • ${timerSeconds}s • ${difficulty.label}"
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

            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.borderGlass, CircleShape)
                        .testTag("quiz_config_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "TEST SETUP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Quiz & Test Configurator",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Live Summary Glass Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp,
                borderGlow = true,
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(palette.primaryAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "LIVE SPECIFICATION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = liveSummary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = palette.primaryAccent
                            )
                        }
                    }
                    GlassBadge(text = initialConfig.subject, color = palette.secondaryAccent)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Questions per Quiz
            Text(
                text = "QUESTIONS PER QUIZ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(5, 10, 15, 20).forEach { count ->
                    GlassPill(
                        text = "$count Qs",
                        isSelected = questionCount == count,
                        onClick = { questionCount = count },
                        modifier = Modifier.weight(1f),
                        testTag = "config_q_$count"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Options per MCQ
            Text(
                text = "OPTIONS PER MCQ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(2, 3, 4, 5).forEach { opt ->
                    GlassPill(
                        text = "$opt Opts",
                        isSelected = optionsCount == opt,
                        onClick = { optionsCount = opt },
                        modifier = Modifier.weight(1f),
                        testTag = "config_opt_$opt"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Timer per Question
            Text(
                text = "TIMER PER QUESTION",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            val timerOptions = listOf(10, 30, 45, 60, 90, 120, 180)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(timerOptions) { sec ->
                    GlassPill(
                        text = "${sec}s",
                        isSelected = timerSeconds == sec,
                        onClick = { timerSeconds = sec },
                        testTag = "config_timer_$sec"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Exam Difficulty Level
            Text(
                text = "EXAM DIFFICULTY LEVEL",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Difficulty.entries.forEach { diff ->
                    val isSelected = difficulty == diff
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 14.dp,
                        borderGlow = isSelected,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        onClick = { difficulty = diff },
                        testTag = "config_diff_${diff.name.lowercase()}"
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = diff.label,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) palette.primaryAccent else TextWhite
                                )
                                Text(
                                    text = when (diff) {
                                        Difficulty.FOUNDATION -> "Foundational formulas & definitions"
                                        Difficulty.EASY -> "Basic recall & fundamental formulas"
                                        Difficulty.MODERATE -> "Standard NCERT exam questions & balance"
                                        Difficulty.HIGH -> "Application-level & Assertion-Reason traps"
                                        Difficulty.ADVANCED -> "Complex multi-concept problem solving"
                                        Difficulty.OLYMPIAD -> "Advanced multi-step concepts & Olympiad rigor"
                                        Difficulty.ADAPTIVE -> "Auto-scales based on accuracy & mistakes"
                                    },
                                    fontSize = 11.sp,
                                    color = TextGrayBlue
                                )
                            }
                            if (isSelected) {
                                GlassBadge(text = "SELECTED", color = palette.primaryAccent)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // START QUIZ button
            GlassButton(
                text = "START QUIZ ROUND",
                onClick = {
                    val updatedConfig = initialConfig.copy(
                        questionCount = questionCount,
                        optionsCount = optionsCount,
                        timerSecondsPerQuestion = timerSeconds,
                        difficulty = difficulty
                    )
                    onStartQuiz(updatedConfig)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = VoidBlack,
                        modifier = Modifier.size(22.dp)
                    )
                },
                testTag = "config_start_quiz_final_button"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
