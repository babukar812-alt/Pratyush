package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppStorage
import com.example.data.ThinkXIntelligence
import com.example.model.ChallengeConfig
import com.example.model.ChallengeType
import com.example.ui.theme.*

@Composable
fun ChallengeMeDialog(
    storage: AppStorage,
    classLevel: String,
    currentSubject: String,
    currentChapter: String,
    onDismiss: () -> Unit,
    onLaunchChallenge: (ChallengeConfig) -> Unit
) {
    val palette = LocalThemePalette.current
    val challenges = remember(classLevel, currentSubject, currentChapter) {
        ThinkXIntelligence.generatePersonalizedChallenges(classLevel, currentSubject, currentChapter, storage)
    }

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            borderGlow = true,
            cornerRadius = 28.dp,
            contentPadding = PaddingValues(18.dp),
            testTag = "challenge_me_dialog"
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
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
                                .background(AmberGlow.copy(alpha = 0.15f))
                                .border(1.dp, AmberGlow.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EmojiEvents,
                                contentDescription = null,
                                tint = AmberGlow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AI ACADEMIC ARENA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGlow,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = "Challenge Me",
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

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "High-yield personalized challenges engineered around your recent learning data.",
                    fontSize = 12.sp,
                    color = TextGrayBlue
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(challenges) { challenge ->
                        ChallengeCard(
                            challenge = challenge,
                            onClick = {
                                onDismiss()
                                onLaunchChallenge(challenge)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeCard(
    challenge: ChallengeConfig,
    onClick: () -> Unit
) {
    val palette = LocalThemePalette.current
    val (icon, accentColor) = when (challenge.type) {
        ChallengeType.WEAK_AREA -> Pair(Icons.Rounded.TrackChanges, CrimsonAlert)
        ChallengeType.SPEED -> Pair(Icons.Rounded.FlashOn, AmberGlow)
        ChallengeType.PERFECT_SCORE -> Pair(Icons.Rounded.Stars, palette.secondaryAccent)
        ChallengeType.HARD_MODE -> Pair(Icons.Rounded.MilitaryTech, palette.primaryAccent)
        ChallengeType.MISTAKE -> Pair(Icons.Rounded.Replay, palette.primaryAccent)
        ChallengeType.MIXED_SUBJECT -> Pair(Icons.Rounded.AllInclusive, palette.secondaryAccent)
        ChallengeType.CHAPTER -> Pair(Icons.Rounded.MenuBook, SoftGreen)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(palette.surfaceGlass)
            .border(1.dp, palette.borderGlass, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f))
                            .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = challenge.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = challenge.subtitle, fontSize = 11.sp, color = TextGrayBlue)
                    }
                }

                GlassBadge(text = "+${challenge.xpReward} XP", color = AmberGlow)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = challenge.description,
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.85f),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Quiz, contentDescription = null, tint = TextGrayBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${challenge.questionCount} Questions", fontSize = 11.sp, color = TextGrayBlue)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Timer, contentDescription = null, tint = TextGrayBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${challenge.timerSecondsPerQuestion}s / Q", fontSize = 11.sp, color = TextGrayBlue)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Start", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accentColor)
                    Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
