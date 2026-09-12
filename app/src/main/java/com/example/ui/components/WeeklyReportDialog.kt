package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppStorage
import com.example.data.ThinkXIntelligence
import com.example.model.WeeklyReport
import com.example.ui.theme.*

@Composable
fun WeeklyReportDialog(
    storage: AppStorage,
    onDismiss: () -> Unit,
    onStartPractice: () -> Unit
) {
    val palette = LocalThemePalette.current
    val report = remember { ThinkXIntelligence.getWeeklyProgress(storage) }

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            borderGlow = true,
            cornerRadius = 28.dp,
            contentPadding = PaddingValues(20.dp),
            testTag = "weekly_report_dialog"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
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
                                .background(palette.primaryAccent.copy(alpha = 0.15f))
                                .border(1.dp, palette.primaryAccent.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Insights,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ACADEMIC INTELLIGENCE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.primaryAccent,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = "Your ThinkX Week",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!report.isEligible) {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, RoundedCornerShape(20.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Rounded.Analytics,
                                contentDescription = null,
                                tint = palette.primaryAccent.copy(alpha = 0.6f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Building Your Weekly Baseline",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = report.insight,
                                fontSize = 12.sp,
                                color = TextGrayBlue,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            GlassButton(
                                text = "START FIRST QUIZ",
                                onClick = {
                                    onDismiss()
                                    onStartPractice()
                                }
                            )
                        }
                    }
                } else {
                    // Insight Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        palette.primaryAccent.copy(alpha = 0.16f),
                                        palette.secondaryAccent.copy(alpha = 0.12f)
                                    )
                                )
                            )
                            .border(1.dp, palette.primaryAccent.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Rounded.Lightbulb,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "THINKX INSIGHT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = palette.primaryAccent,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = report.insight,
                                    fontSize = 12.sp,
                                    color = TextWhite,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metrics Grid (2 columns)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ReportMetricCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.Quiz,
                            title = "Quizzes Completed",
                            value = "${report.quizzesCompleted}",
                            subtitle = "${report.totalSessions} study sessions",
                            color = palette.primaryAccent
                        )
                        ReportMetricCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.QueryStats,
                            title = "Average Score",
                            value = "${report.averageScore}%",
                            subtitle = "NCERT assessment avg",
                            color = if (report.averageScore >= 75) SoftGreen else AmberGlow
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ReportMetricCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.Schedule,
                            title = "Study Time",
                            value = "${report.studyTimeMinutes} min",
                            subtitle = "Focused practice",
                            color = palette.secondaryAccent
                        )
                        ReportMetricCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.CheckCircleOutline,
                            title = "Mistakes Improved",
                            value = "${report.mistakesImproved}",
                            subtitle = "Mastered concepts",
                            color = SoftGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ReportMetricCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.TrendingUp,
                            title = "Strongest Subject",
                            value = report.strongestSubject,
                            subtitle = report.strongestChapter,
                            color = palette.primaryAccent
                        )
                        ReportMetricCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Rounded.TrackChanges,
                            title = "Weak Area Focus",
                            value = report.weakestSubject,
                            subtitle = report.weakestChapter,
                            color = CrimsonAlert
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Streak and XP strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.LocalFireDepartment, contentDescription = null, tint = AmberGlow, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Streak: ${report.streak} Days Active", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "+${report.xpEarned} XP Earned", fontSize = 12.sp, color = palette.primaryAccent, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    GlassButton(
                        text = "START RECOMMENDED PRACTICE",
                        onClick = {
                            onDismiss()
                            onStartPractice()
                        },
                        leadingIcon = {
                            Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = VoidBlack, modifier = Modifier.size(20.dp))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportMetricCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String,
    color: Color
) {
    val palette = LocalThemePalette.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(palette.surfaceGlass)
            .border(1.dp, palette.borderGlass, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, fontSize = 10.sp, color = TextGrayBlue, fontWeight = FontWeight.Medium, maxLines = 1)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 17.sp, fontWeight = FontWeight.Black, color = TextWhite, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 10.sp, color = TextGrayBlue, maxLines = 1)
        }
    }
}
