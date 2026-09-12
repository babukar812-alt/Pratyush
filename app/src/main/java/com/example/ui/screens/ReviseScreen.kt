package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStorage
import com.example.data.ThinkXIntelligence
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class RevisionTopic(
    val subject: String,
    val chapter: String,
    val progress: Float,
    val status: String,
    val masteryLabel: String
)

enum class ReviseTab(val label: String) {
    SPACED_REPETITION("Weak Areas"),
    MISTAKE_BANK("Mistake Bank"),
    REVISION_PACKS("AI Summaries")
}

@Composable
fun ReviseScreen(
    userProfile: UserProfile,
    onStartRevisionQuiz: (String, String) -> Unit,
    onPracticeMistakes: ((List<Question>) -> Unit)? = null,
    onSaveCard: ((Flashcard) -> Unit)? = null
) {
    val palette = LocalThemePalette.current
    val context = LocalContext.current
    val storage = remember { AppStorage(context) }
    val coroutineScope = rememberCoroutineScope()

    var activeTab by remember { mutableStateOf(ReviseTab.SPACED_REPETITION) }
    var mistakeFilter by remember { mutableStateOf("ALL") } // "ALL", "NEEDS_REVIEW", "IMPROVING", "MASTERED"
    var saveFeedback by remember { mutableStateOf<String?>(null) }

    val allMistakes = remember { storage.getMistakes() }
    val filteredMistakes = remember(allMistakes, mistakeFilter) {
        when (mistakeFilter) {
            "NEEDS_REVIEW" -> allMistakes.filter { it.status == "Needs review" }
            "IMPROVING" -> allMistakes.filter { it.status == "Improving" }
            "MASTERED" -> allMistakes.filter { it.status == "Mastered" }
            else -> allMistakes
        }
    }

    // Default academic revision pack
    val revisionPack = remember {
        ThinkXIntelligence.generateRevisionPack(
            classLevel = userProfile.classLevel,
            subject = "Science",
            chapter = "Chemical Reactions and Equations"
        )
    }

    val continueTopics = listOf(
        RevisionTopic("Science", "Chemical Reactions and Equations", 0.85f, "Ready for Recall", "Strong"),
        RevisionTopic("Math", "Introduction to Trigonometry", 0.60f, "Practice Formulas", "Moderate")
    )

    val weakAreas = listOf(
        RevisionTopic("Physics", "Light – Reflection & Ray Diagrams", 0.35f, "Need Review", "Needs Focus"),
        RevisionTopic("Chemistry", "Acids, Bases & pH Calculations", 0.45f, "Low Accuracy", "Needs Focus")
    )

    LiquidBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "INTELLIGENT STUDY ECOSYSTEM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Revise & Master",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Turn mistakes into strengths with personalized recall loops.",
                        fontSize = 13.sp,
                        color = TextGrayBlue
                    )
                }
            }

            // Tab Switcher
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.borderGlass, RoundedCornerShape(16.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReviseTab.entries.forEach { tab ->
                        val isSelected = activeTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .then(
                                    if (isSelected) {
                                        Modifier.background(
                                            Brush.horizontalGradient(listOf(palette.primaryAccent, palette.secondaryAccent))
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                                .clickable { activeTab = tab }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) VoidBlack else TextGrayBlue
                            )
                        }
                    }
                }
            }

            // Feedback Toast
            if (saveFeedback != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SoftGreen.copy(alpha = 0.2f))
                            .border(1.dp, SoftGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "✓ $saveFeedback",
                            color = SoftGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            when (activeTab) {
                ReviseTab.SPACED_REPETITION -> {
                    // Weak Areas
                    item {
                        SectionHeader(title = "CRITICAL WEAK AREAS", icon = Icons.Rounded.WarningAmber, color = SoftRed)
                    }
                    items(weakAreas) { topic ->
                        RevisionCard(
                            topic = topic,
                            onRevise = { onStartRevisionQuiz(topic.subject, topic.chapter) }
                        )
                    }

                    // Scheduled Recall
                    item {
                        SectionHeader(title = "CONTINUE SCHEDULED RECALL", icon = Icons.Rounded.PlayCircle)
                    }
                    items(continueTopics) { topic ->
                        RevisionCard(
                            topic = topic,
                            onRevise = { onStartRevisionQuiz(topic.subject, topic.chapter) }
                        )
                    }
                }

                ReviseTab.MISTAKE_BANK -> {
                    // Mistake Bank Header Card
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            borderGlow = true,
                            cornerRadius = 20.dp,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "MISTAKE BANK AUDIT",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SoftRed,
                                        letterSpacing = 1.2.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${allMistakes.size} Questions Missed",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }

                                if (allMistakes.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(palette.primaryAccent, palette.secondaryAccent)
                                                )
                                            )
                                            .clickable {
                                                val questions = ThinkXIntelligence.generateMistakeQuiz(allMistakes)
                                                onPracticeMistakes?.invoke(questions)
                                            }
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                            .testTag("practice_all_mistakes_btn"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Practice All",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VoidBlack
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Filter Pills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                FilterChip(
                                    label = "All (${allMistakes.size})",
                                    isSelected = mistakeFilter == "ALL",
                                    onClick = { mistakeFilter = "ALL" }
                                )
                                FilterChip(
                                    label = "Needs Review (${allMistakes.count { it.status == "Needs review" }})",
                                    isSelected = mistakeFilter == "NEEDS_REVIEW",
                                    onClick = { mistakeFilter = "NEEDS_REVIEW" }
                                )
                                FilterChip(
                                    label = "Mastered (${allMistakes.count { it.status == "Mastered" }})",
                                    isSelected = mistakeFilter == "MASTERED",
                                    onClick = { mistakeFilter = "MASTERED" }
                                )
                            }
                        }
                    }

                    if (filteredMistakes.isEmpty()) {
                        item {
                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(24.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = SoftGreen, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Mistake Bank is Clear!",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "Keep taking quizzes to identify any emerging blind spots.",
                                        fontSize = 12.sp,
                                        color = TextGrayBlue
                                    )
                                }
                            }
                        }
                    } else {
                        items(filteredMistakes) { mistake ->
                            MistakeCard(
                                mistake = mistake,
                                onPracticeSingle = {
                                    val questions = ThinkXIntelligence.generateMistakeQuiz(listOf(mistake))
                                    onPracticeMistakes?.invoke(questions)
                                }
                            )
                        }
                    }
                }

                ReviseTab.REVISION_PACKS -> {
                    // AI NCERT Revision Pack
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            borderGlow = true,
                            cornerRadius = 20.dp,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        GlassBadge(text = revisionPack.subject.uppercase(), color = palette.primaryAccent)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        GlassBadge(text = "CLASS ${revisionPack.classLevel}", color = palette.secondaryAccent)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = revisionPack.chapter,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = revisionPack.summary,
                                        fontSize = 12.sp,
                                        color = TextGrayBlue,
                                        lineHeight = 16.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val card = Flashcard(
                                            subject = revisionPack.subject,
                                            concept = revisionPack.chapter,
                                            explanation = revisionPack.keyConcepts.joinToString("\n• "),
                                            isCustom = true
                                        )
                                        onSaveCard?.invoke(card)
                                        saveFeedback = "Saved Key Concepts to Flashcards!"
                                        coroutineScope.launch {
                                            delay(2000)
                                            saveFeedback = null
                                        }
                                    },
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(palette.primaryAccent.copy(alpha = 0.2f))
                                ) {
                                    Icon(Icons.Rounded.BookmarkAdd, contentDescription = "Save Cards", tint = palette.primaryAccent)
                                }
                            }
                        }
                    }

                    // Key Concepts
                    item {
                        SectionHeader(title = "CORE NCERT CONCEPTS", icon = Icons.Rounded.Lightbulb)
                    }
                    items(revisionPack.keyConcepts) { concept ->
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(text = "•", color = palette.primaryAccent, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = concept, color = TextWhite, fontSize = 13.sp, lineHeight = 18.sp)
                            }
                        }
                    }

                    // Governing Equations & Formulas
                    item {
                        SectionHeader(title = "GOVERNING FORMULAS & REACTIONS", icon = Icons.Rounded.Functions)
                    }
                    items(revisionPack.formulas) { formula ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(palette.surfaceGlass)
                                .border(1.dp, palette.primaryAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = formula,
                                color = palette.primaryAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Common Board Traps
                    item {
                        SectionHeader(title = "EXAM MISTAKE TRAPS TO AVOID", icon = Icons.Rounded.Warning, color = SoftRed)
                    }
                    items(revisionPack.commonMistakes) { trap ->
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Rounded.Close, contentDescription = null, tint = SoftRed, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = trap, color = TextWhite, fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MistakeCard(
    mistake: Mistake,
    onPracticeSingle: () -> Unit
) {
    val palette = LocalThemePalette.current
    val statusColor = when (mistake.status) {
        "Mastered" -> SoftGreen
        "Improving" -> AmberGlow
        else -> SoftRed
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp,
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${mistake.subject} • ${mistake.chapter}",
                fontSize = 11.sp,
                color = palette.primaryAccent,
                fontWeight = FontWeight.SemiBold
            )
            GlassBadge(text = mistake.status.uppercase(), color = statusColor)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = mistake.question,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            lineHeight = 19.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Options overview
        if (mistake.correctOptionIndex in mistake.options.indices) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SoftGreen.copy(alpha = 0.15f))
                    .border(1.dp, SoftGreen.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Correct: ${mistake.options[mistake.correctOptionIndex]}",
                    color = SoftGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = mistake.explanation,
            fontSize = 11.sp,
            color = TextGrayBlue,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(palette.surfaceGlass)
                    .border(1.dp, palette.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .clickable { onPracticeSingle() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Practice Again",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryAccent
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val palette = LocalThemePalette.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) palette.primaryAccent.copy(alpha = 0.25f) else palette.surfaceGlass)
            .border(1.dp, if (isSelected) palette.primaryAccent else palette.borderGlass, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) palette.primaryAccent else TextGrayBlue
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color = LocalThemePalette.current.primaryAccent
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
private fun RevisionCard(
    topic: RevisionTopic,
    onRevise: () -> Unit,
    testTag: String? = null
) {
    val palette = LocalThemePalette.current

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(14.dp),
        testTag = testTag
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlassBadge(text = topic.subject.uppercase(), color = palette.primaryAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = topic.masteryLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (topic.progress > 0.7f) SoftGreen else SoftRed
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = topic.chapter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1
                )
                Text(
                    text = topic.status,
                    fontSize = 11.sp,
                    color = TextGrayBlue
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(palette.primaryAccent)
                    .clickable { onRevise() }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Revise",
                    color = VoidBlack,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        GlassProgress(progress = topic.progress, height = 5.dp)
    }
}
