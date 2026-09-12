package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuizResult
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun QuizResultScreen(
    result: QuizResult,
    onPlayAgain: () -> Unit,
    onBackToDashboard: () -> Unit,
    onDiscussWithAI: () -> Unit,
    onPracticeMistakes: (() -> Unit)? = null
) {
    val palette = LocalThemePalette.current
    val scrollState = rememberScrollState()

    var startAnimation by remember { mutableStateOf(false) }
    val animatedPercent by animateFloatAsState(
        targetValue = if (startAnimation) result.percentage.toFloat() else 0f,
        animationSpec = tween(1200),
        label = "percentAnim"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    LiquidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "QUIZ SUMMARY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryAccent,
                letterSpacing = 2.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Round Completed",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Animated Circular Score Card
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .shadow(16.dp, CircleShape, spotColor = palette.primaryAccent)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                palette.primaryAccent.copy(alpha = 0.25f),
                                palette.surfaceGlass.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .border(
                        width = 3.dp,
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                palette.primaryAccent,
                                palette.secondaryAccent,
                                palette.primaryAccent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${animatedPercent.toInt()}%",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Text(
                        text = "${result.score} PTS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // XP Gain Badge
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AmberGlow.copy(alpha = 0.15f))
                    .border(1.dp, AmberGlow.copy(alpha = 0.4f), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Stars,
                    contentDescription = null,
                    tint = AmberGlow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "+${result.xpEarned} XP AWARDED",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AmberGlow,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Metric Breakdown Grid
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlow = true,
                cornerRadius = 20.dp,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "PERFORMANCE METRICS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryAccent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricItem(label = "CORRECT", value = "${result.correctCount}", color = SoftGreen, icon = Icons.Rounded.CheckCircle)
                    MetricItem(label = "INCORRECT", value = "${result.incorrectCount}", color = SoftRed, icon = Icons.Rounded.Cancel)
                    MetricItem(label = "SKIPPED", value = "${result.skippedCount}", color = TextMuted, icon = Icons.Rounded.RemoveCircleOutline)
                    MetricItem(label = "TIME", value = "${result.timeSpentSeconds}s", color = palette.primaryAccent, icon = Icons.Rounded.Schedule)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details Metadata Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlow = false,
                cornerRadius = 16.dp,
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Subject", fontSize = 12.sp, color = TextGrayBlue)
                    Text(text = result.subject, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Chapter", fontSize = 12.sp, color = TextGrayBlue)
                    Text(text = result.chapter, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Difficulty", fontSize = 12.sp, color = TextGrayBlue)
                    Text(text = result.difficulty, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = palette.secondaryAccent)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons: PLAY AGAIN, DISCUSS WITH AI, BACK TO DASHBOARD
            GlassButton(
                text = "PLAY AGAIN",
                onClick = onPlayAgain,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Replay,
                        contentDescription = null,
                        tint = VoidBlack,
                        modifier = Modifier.size(18.dp)
                    )
                },
                testTag = "result_play_again_button"
            )

            if (result.incorrectCount > 0 && onPracticeMistakes != null) {
                Spacer(modifier = Modifier.height(10.dp))
                GlassButton(
                    text = "PRACTICE ${result.incorrectCount} MISTAKE${if (result.incorrectCount > 1) "S" else ""}",
                    onClick = onPracticeMistakes,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Spellcheck,
                            contentDescription = null,
                            tint = VoidBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    testTag = "result_practice_mistakes_button"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            GlassButton(
                text = "DISCUSS RESULTS WITH AI",
                onClick = onDiscussWithAI,
                isSecondary = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = palette.primaryAccent,
                        modifier = Modifier.size(18.dp)
                    )
                },
                testTag = "result_discuss_ai_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            GlassButton(
                text = "BACK TO DASHBOARD",
                onClick = onBackToDashboard,
                isSecondary = true,
                testTag = "result_back_dashboard_button"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MetricItem(
    label: String,
    value: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextWhite)
        Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color, letterSpacing = 0.5.sp)
    }
}
