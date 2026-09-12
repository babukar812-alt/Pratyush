package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.model.UserProfile
import com.example.ui.components.*
import com.example.ui.theme.*

data class AcademicBadge(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean
)

@Composable
fun BadgesScreen(
    userProfile: UserProfile
) {
    val palette = LocalThemePalette.current

    val xpForNext = 250
    val currentLevel = (userProfile.xp / xpForNext) + 1
    val currentLevelXp = userProfile.xp % xpForNext
    val levelProgress = currentLevelXp.toFloat() / xpForNext

    val allBadges = remember(userProfile) {
        listOf(
            AcademicBadge("b1", "First Quiz", "Completed your very first ThinkX quiz round.", Icons.Rounded.PlayCircle, isUnlocked = userProfile.totalQuizzes >= 1),
            AcademicBadge("b2", "Speed Demon", "Answered 5 questions in under 10 seconds each.", Icons.Rounded.FlashOn, isUnlocked = userProfile.totalCorrect >= 5),
            AcademicBadge("b3", "Science Master", "Scored 90%+ in any Class 10 Science quiz.", Icons.Rounded.Biotech, isUnlocked = userProfile.totalQuizzes >= 2),
            AcademicBadge("b4", "Math Explorer", "Completed a full geometry and trigonometry set.", Icons.Rounded.Calculate, isUnlocked = userProfile.totalQuestionsAnswered >= 10),
            AcademicBadge("b5", "Streak Flame", "Maintained an unbroken 3-day revision streak.", Icons.Rounded.LocalFireDepartment, isUnlocked = userProfile.streak >= 3),
            AcademicBadge("b6", "Perfect 100", "Achieved a flawless 100% score on a full test.", Icons.Rounded.Stars, isUnlocked = userProfile.totalCorrect >= 10),
            AcademicBadge("b7", "Night Owl", "Completed an NCERT revision session after 9 PM.", Icons.Rounded.NightsStay, isUnlocked = true),
            AcademicBadge("b8", "NCERT Scholar", "Answered more than 50 total NCERT questions.", Icons.Rounded.WorkspacePremium, isUnlocked = userProfile.totalQuestionsAnswered >= 50)
        )
    }

    var selectedBadge by remember { mutableStateOf<AcademicBadge?>(null) }

    LiquidBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "ACHIEVEMENTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Badges & Ranks",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Level up through quizzes, daily streaks & accuracy.",
                        fontSize = 13.sp,
                        color = TextGrayBlue
                    )
                }
            }

            // Level & XP Progress Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderGlow = true,
                    cornerRadius = 24.dp,
                    contentPadding = PaddingValues(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                GlassBadge(text = "LEVEL $currentLevel", color = palette.primaryAccent)
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(AmberGlow.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Rounded.LocalFireDepartment, contentDescription = null, tint = AmberGlow, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "${userProfile.streak} Day Streak", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberGlow)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${userProfile.xp} TOTAL XP",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .shadow(8.dp, CircleShape, spotColor = palette.primaryAccent)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(palette.primaryAccent, palette.secondaryAccent)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "L$currentLevel",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = VoidBlack
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Next: Level ${currentLevel + 1}",
                            fontSize = 12.sp,
                            color = TextGrayBlue
                        )
                        Text(
                            text = "$currentLevelXp / $xpForNext XP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    GlassProgress(progress = levelProgress, height = 8.dp)
                }
            }

            // Daily Quests Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 18.dp,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = "DAILY OBJECTIVES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    QuestItem(title = "Complete 1 NCERT Quiz Round", xp = "+20 XP", isDone = userProfile.totalQuizzes >= 1)
                    Spacer(modifier = Modifier.height(8.dp))
                    QuestItem(title = "Answer 5 questions correctly", xp = "+50 XP", isDone = userProfile.totalCorrect >= 5)
                    Spacer(modifier = Modifier.height(8.dp))
                    QuestItem(title = "Ask ThinkX AI a concept doubt", xp = "+15 XP", isDone = true)
                }
            }

            // Badges Grid Section Header
            item {
                val unlockedCount = allBadges.count { it.isUnlocked }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACADEMIC BADGES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$unlockedCount / ${allBadges.size} UNLOCKED",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }
            }

            // Badges Grid
            items(allBadges.chunked(2).size) { rowIndex ->
                val rowBadges = allBadges.chunked(2)[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowBadges.forEach { badge ->
                        BadgeCard(
                            badge = badge,
                            onClick = { selectedBadge = badge },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowBadges.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

    // Badge Details Modal
    if (selectedBadge != null) {
        val b = selectedBadge!!
        Dialog(onDismissRequest = { selectedBadge = null }) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                borderGlow = b.isUnlocked,
                cornerRadius = 24.dp,
                contentPadding = PaddingValues(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                if (b.isUnlocked) palette.primaryAccent.copy(alpha = 0.25f)
                                else palette.surfaceGlass
                            )
                            .border(
                                2.dp,
                                if (b.isUnlocked) palette.primaryAccent else palette.borderGlass,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = b.icon,
                            contentDescription = null,
                            tint = if (b.isUnlocked) palette.primaryAccent else TextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = b.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    GlassBadge(
                        text = if (b.isUnlocked) "UNLOCKED" else "LOCKED",
                        color = if (b.isUnlocked) SoftGreen else TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = b.description,
                        fontSize = 13.sp,
                        color = TextGrayBlue,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    GlassButton(
                        text = "CLOSE",
                        onClick = { selectedBadge = null },
                        isSecondary = true
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestItem(title: String, xp: String, isDone: Boolean) {
    val palette = LocalThemePalette.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(palette.surfaceGlass.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = if (isDone) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isDone) SoftGreen else TextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (isDone) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isDone) TextWhite else TextGrayBlue
            )
        }
        GlassBadge(text = xp, color = AmberGlow)
    }
}

@Composable
private fun BadgeCard(
    badge: AcademicBadge,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalThemePalette.current

    GlassCard(
        modifier = modifier,
        cornerRadius = 18.dp,
        borderGlow = badge.isUnlocked,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick,
        testTag = "badge_${badge.id}"
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.isUnlocked) palette.primaryAccent.copy(alpha = 0.2f)
                        else palette.surfaceGlass
                    )
                    .border(
                        1.dp,
                        if (badge.isUnlocked) palette.primaryAccent else palette.borderGlass,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badge.icon,
                    contentDescription = null,
                    tint = if (badge.isUnlocked) palette.primaryAccent else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = badge.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (badge.isUnlocked) TextWhite else TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (badge.isUnlocked) "Achieved" else "Locked",
                fontSize = 11.sp,
                color = if (badge.isUnlocked) SoftGreen else TextMuted
            )
        }
    }
}
