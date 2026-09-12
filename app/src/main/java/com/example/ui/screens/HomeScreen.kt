package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppStorage
import com.example.data.CurriculumData
import com.example.data.ThinkXIntelligence
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    selectedClass: String,
    onClassSelected: (String) -> Unit,
    selectedSubject: String,
    onSubjectSelected: (String) -> Unit,
    selectedChapter: String,
    onChapterSelected: (String) -> Unit,
    onLaunchConfigurator: () -> Unit,
    onOpenProfile: () -> Unit,
    onStartQuickQuiz: ((String, String, String) -> Unit)? = null,
    onNavigateToMistakes: (() -> Unit)? = null,
    onNavigateToAI: ((String, String, String) -> Unit)? = null,
    onNavigateToRevise: (() -> Unit)? = null,
    onNavigateToCards: (() -> Unit)? = null,
    onStartChallenge: ((ChallengeConfig) -> Unit)? = null
) {
    val palette = LocalThemePalette.current
    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    val classes = remember { CurriculumData.getClasses() }
    val subjects = remember(selectedClass) {
        CurriculumData.getSubjects(selectedClass)
    }
    val chapters = remember(selectedClass, selectedSubject) {
        CurriculumData.getChapterNames(selectedClass, selectedSubject, includeAll = true)
    }

    // Dynamic ecosystem state
    var dailyPulse by remember { mutableStateOf(storage.getDailyPulse()) }
    var studyPlan by remember(selectedClass, selectedSubject, selectedChapter) {
        mutableStateOf(storage.getStudyPlan(selectedClass, selectedSubject, selectedChapter))
    }
    val lastSession = remember { storage.getLastStudiedSession(selectedClass) }
    val activeMistakes = remember { storage.getMistakes().filter { it.status == "Needs review" } }
    val recommendations = remember(selectedClass, selectedSubject) {
        ThinkXIntelligence.getSmartRecommendations(storage, selectedClass, selectedSubject)
    }

    // Chapter Hub Modal Sheet state
    var hubChapter by remember { mutableStateOf<String?>(null) }

    // ThinkX Intelligence Engine Dialog states
    var showCommandCenter by remember { mutableStateOf(false) }
    var showWeeklyReport by remember { mutableStateOf(false) }
    var showMasteryMap by remember { mutableStateOf(false) }
    var showChallengeDialog by remember { mutableStateOf(false) }
    var showFocusMode by remember { mutableStateOf(false) }

    LiquidBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Header: Dashboard & User profile
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "THINKX INTELLIGENCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Study Engine",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    // Profile Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, RoundedCornerShape(24.dp))
                            .clickable { onOpenProfile() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlassAvatar(initials = userProfile.avatarInitials, size = 32.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = userProfile.username,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Class ${userProfile.classLevel}",
                                fontSize = 10.sp,
                                color = palette.primaryAccent
                            )
                        }
                    }
                }
            }

            // COMMAND CENTER SEARCH BAR
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.borderGlass, RoundedCornerShape(16.dp))
                        .clickable { showCommandCenter = true }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .testTag("home_command_search_bar")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Ask ThinkX anything or command an action...",
                                fontSize = 12.sp,
                                color = TextGrayBlue
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(palette.primaryAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "CMD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.primaryAccent
                            )
                        }
                    }
                }
            }

            // INTELLIGENCE QUICK ACTIONS RIBBON
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IntelligenceRibbonChip(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.EmojiEvents,
                        label = "Challenge Me",
                        color = AmberGlow,
                        onClick = { showChallengeDialog = true }
                    )
                    IntelligenceRibbonChip(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.AccountTree,
                        label = "Mastery Map",
                        color = palette.secondaryAccent,
                        onClick = { showMasteryMap = true }
                    )
                    IntelligenceRibbonChip(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Insights,
                        label = "Weekly Report",
                        color = palette.primaryAccent,
                        onClick = { showWeeklyReport = true }
                    )
                    IntelligenceRibbonChip(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.SelfImprovement,
                        label = "Deep Focus",
                        color = SoftGreen,
                        onClick = { showFocusMode = true }
                    )
                }
            }

            // 2. DAILY STUDY PULSE CARD
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DAILY STUDY PULSE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.primaryAccent,
                                letterSpacing = 1.2.sp
                            )
                        }
                        Text(
                            text = "Class $selectedClass",
                            fontSize = 11.sp,
                            color = TextGrayBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PulseMetric(
                            icon = Icons.Rounded.LocalFireDepartment,
                            label = "STREAK",
                            value = "${dailyPulse.streak}d",
                            color = AmberGlow
                        )
                        PulseMetric(
                            icon = Icons.Rounded.ElectricBolt,
                            label = "TODAY'S XP",
                            value = "+${dailyPulse.xpToday}",
                            color = palette.secondaryAccent
                        )
                        PulseMetric(
                            icon = Icons.Rounded.TaskAlt,
                            label = "QUIZZES",
                            value = "${dailyPulse.quizzesToday}",
                            color = palette.primaryAccent
                        )
                        PulseMetric(
                            icon = Icons.Rounded.QueryStats,
                            label = "ACCURACY",
                            value = if (dailyPulse.questionsToday > 0) "${dailyPulse.accuracyToday}%" else "--",
                            color = SoftGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = dailyPulse.statusMessage,
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 3. CONTINUE LEARNING (Last studied chapter)
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 20.dp,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CONTINUE LEARNING",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.secondaryAccent,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lastSession.chapter,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                maxLines = 1
                            )
                            Text(
                                text = "${lastSession.subject} • Class ${lastSession.classLevel}",
                                fontSize = 12.sp,
                                color = TextGrayBlue
                            )
                        }

                        // Continue round button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(palette.primaryAccent, palette.secondaryAccent)
                                    )
                                )
                                .clickable {
                                    onClassSelected(lastSession.classLevel)
                                    onSubjectSelected(lastSession.subject)
                                    onChapterSelected(lastSession.chapter)
                                    onLaunchConfigurator()
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("home_continue_learning_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Continue",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VoidBlack
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Rounded.ArrowForward,
                                    contentDescription = null,
                                    tint = VoidBlack,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    GlassProgress(progress = 0.65f, height = 6.dp)
                }
            }

            // 4. TODAY'S STUDY PLAN (Interactive Checklist)
            item {
                val completedCount = studyPlan.count { it.isCompleted }
                val totalCount = studyPlan.size
                val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
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
                                text = "TODAY'S STUDY PLAN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.primaryAccent,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$completedCount of $totalCount Completed",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                        GlassBadge(text = "+${studyPlan.sumOf { it.xpReward }} XP TOTAL", color = AmberGlow)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    GlassProgress(progress = progressFraction, height = 5.dp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        studyPlan.forEach { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(palette.surfaceGlass.copy(alpha = 0.6f))
                                    .border(
                                        1.dp,
                                        if (task.isCompleted) SoftGreen.copy(alpha = 0.4f) else palette.borderGlass,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        studyPlan = storage.toggleStudyPlanItem(task.id, studyPlan)
                                        dailyPulse = storage.getDailyPulse()
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (task.isCompleted) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (task.isCompleted) SoftGreen else TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = task.title,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (task.isCompleted) TextGrayBlue else TextWhite
                                        )
                                        Text(
                                            text = task.subtitle,
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Text(
                                    text = "+${task.xpReward} XP",
                                    fontSize = 11.sp,
                                    color = if (task.isCompleted) SoftGreen else palette.primaryAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 5. SMART RECOMMENDATIONS (Context-Aware Insights)
            if (recommendations.isNotEmpty()) {
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "RECOMMENDED FOR YOU",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.primaryAccent,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "AI Curated",
                                fontSize = 11.sp,
                                color = palette.secondaryAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        recommendations.forEach { rec ->
                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                cornerRadius = 16.dp,
                                contentPadding = PaddingValues(14.dp),
                                onClick = {
                                    when (rec.type) {
                                        RecommendationType.PRACTICE_MISTAKES -> onNavigateToMistakes?.invoke()
                                        RecommendationType.CONTINUE_LEARNING -> {
                                            onSubjectSelected(rec.subject)
                                            onChapterSelected(rec.chapter)
                                            onLaunchConfigurator()
                                        }
                                        RecommendationType.QUICK_PRACTICE -> {
                                            onStartQuickQuiz?.invoke(selectedClass, rec.subject, rec.chapter)
                                        }
                                        else -> onNavigateToRevise?.invoke()
                                    }
                                }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = rec.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = rec.description,
                                            fontSize = 11.sp,
                                            color = TextGrayBlue,
                                            lineHeight = 16.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    GlassBadge(
                                        text = rec.actionLabel,
                                        color = if (rec.type == RecommendationType.PRACTICE_MISTAKES) SoftRed else palette.primaryAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. MAIN TEST PAPER LAUNCH CARD
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
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                GlassBadge(text = "NCERT SYLLABUS", color = palette.primaryAccent)
                                Spacer(modifier = Modifier.width(8.dp))
                                GlassBadge(text = "CLASS $selectedClass", color = palette.secondaryAccent)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "FULL TEST PAPER",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Text(
                                text = "$selectedSubject • $selectedChapter",
                                fontSize = 12.sp,
                                color = palette.primaryAccent,
                                maxLines = 1
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(palette.primaryAccent.copy(alpha = 0.15f))
                                .border(1.dp, palette.primaryAccent.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.School,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isPressed by buttonInteractionSource.collectIsPressedAsState()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(if (isPressed) 0.96f else 1.0f)
                            .shadow(
                                elevation = if (isPressed) 4.dp else 12.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = palette.primaryAccent.copy(alpha = 0.4f),
                                spotColor = palette.primaryAccent
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(palette.primaryAccent, palette.secondaryAccent)
                                )
                            )
                            .clickable(
                                interactionSource = buttonInteractionSource,
                                indication = null,
                                onClick = onLaunchConfigurator
                            )
                            .padding(vertical = 15.dp)
                            .testTag("home_start_quiz_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                tint = VoidBlack,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "START MCQ QUIZ ROUND",
                                color = VoidBlack,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // 7. SELECT CLASS / GRADE
            item {
                Column {
                    Text(
                        text = "1. SELECT CLASS / GRADE (NCERT)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(classes) { grade ->
                            val isSelected = selectedClass == grade
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) palette.primaryAccent.copy(alpha = 0.22f) else palette.surfaceGlass
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) palette.primaryAccent else palette.borderGlass,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { onClassSelected(grade) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("class_pill_$grade"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.School,
                                        contentDescription = null,
                                        tint = if (isSelected) palette.primaryAccent else TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Class $grade",
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) TextWhite else TextGrayBlue
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "✓", color = palette.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 8. SELECT SUBJECT
            item {
                Column {
                    Text(
                        text = "2. SELECT SUBJECT (Class $selectedClass)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(subjects) { sub ->
                            val isSelected = selectedSubject.equals(sub, ignoreCase = true)
                            val icon = getSubjectIcon(sub)

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) palette.primaryAccent.copy(alpha = 0.2f) else palette.surfaceGlass
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) palette.primaryAccent else palette.borderGlass,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { onSubjectSelected(sub) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("subject_pill_${sub.lowercase().replace(" ", "_")}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) palette.primaryAccent else TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = sub,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) TextWhite else TextGrayBlue
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 9. CHAPTERS LIST (with Mastery Badges & Chapter Hub launcher)
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3. NCERT CHAPTERS & MASTERY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${chapters.size} topics",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        chapters.forEachIndexed { index, ch ->
                            val isSelected = selectedChapter == ch
                            val mastery = remember(selectedClass, selectedSubject, ch) {
                                ThinkXIntelligence.calculateChapterMastery(selectedClass, selectedSubject, ch, storage)
                            }

                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                cornerRadius = 14.dp,
                                borderGlow = isSelected,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                onClick = {
                                    onChapterSelected(ch)
                                    hubChapter = ch
                                },
                                testTag = "chapter_card_$index"
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected) palette.primaryAccent else palette.surfaceGlass
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (index == 0) "★" else "$index",
                                                color = if (isSelected) VoidBlack else TextGrayBlue,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = ch,
                                                color = if (isSelected) palette.primaryAccent else TextWhite,
                                                fontSize = 13.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                            Text(
                                                text = "${mastery.overallScore}% Mastery • ${mastery.statusLabel}",
                                                fontSize = 11.sp,
                                                color = if (mastery.overallScore >= 70) SoftGreen else TextGrayBlue
                                            )
                                        }
                                    }

                                    // Action Indicator
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Rounded.CheckCircle,
                                                contentDescription = null,
                                                tint = palette.primaryAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                        }
                                        Icon(
                                            imageVector = Icons.Rounded.Tune,
                                            contentDescription = "Chapter Hub",
                                            tint = palette.primaryAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // CHAPTER HUB DIALOG / MODAL
        if (hubChapter != null) {
            val ch = hubChapter!!
            val mastery = remember(selectedClass, selectedSubject, ch) {
                ThinkXIntelligence.calculateChapterMastery(selectedClass, selectedSubject, ch, storage)
            }

            Dialog(onDismissRequest = { hubChapter = null }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(palette.surfaceGlass.copy(alpha = 0.95f))
                        .border(1.2.dp, palette.primaryAccent.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CHAPTER HUB",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = palette.primaryAccent,
                                    letterSpacing = 1.2.sp
                                )
                                Text(
                                    text = ch,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite,
                                    maxLines = 1
                                )
                            }
                            IconButton(onClick = { hubChapter = null }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Mastery Overview Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(palette.surfaceGlass)
                                .border(1.dp, palette.borderGlass, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                MetricColumn(label = "MASTERY", value = "${mastery.overallScore}%", color = palette.primaryAccent)
                                MetricColumn(label = "ACCURACY", value = "${mastery.accuracyPercent}%", color = SoftGreen)
                                MetricColumn(label = "MISTAKES", value = "${mastery.activeMistakesCount}", color = if (mastery.activeMistakesCount > 0) SoftRed else TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hub Actions
                        GlassButton(
                            text = "START MCQ QUIZ",
                            onClick = {
                                hubChapter = null
                                onChapterSelected(ch)
                                onLaunchConfigurator()
                            },
                            leadingIcon = {
                                Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = VoidBlack, modifier = Modifier.size(18.dp))
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        GlassButton(
                            text = "TEACH ME INTERACTIVELY",
                            onClick = {
                                hubChapter = null
                                onChapterSelected(ch)
                                onNavigateToAI?.invoke(selectedClass, selectedSubject, ch)
                            },
                            isSecondary = true,
                            leadingIcon = {
                                Icon(Icons.Rounded.School, contentDescription = null, tint = palette.primaryAccent, modifier = Modifier.size(18.dp))
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        GlassButton(
                            text = "VIEW FLASHCARDS",
                            onClick = {
                                hubChapter = null
                                onNavigateToCards?.invoke()
                            },
                            isSecondary = true,
                            leadingIcon = {
                                Icon(Icons.Rounded.Style, contentDescription = null, tint = palette.secondaryAccent, modifier = Modifier.size(18.dp))
                            }
                        )
                    }
                }
            }
        }

        // THINKX COMMAND CENTER DIALOG
        if (showCommandCenter) {
            ThinkXCommandCenterDialog(
                storage = storage,
                classLevel = selectedClass,
                currentSubject = selectedSubject,
                currentChapter = selectedChapter,
                onDismiss = { showCommandCenter = false },
                onExecuteAction = { action ->
                    when (action) {
                        is ThinkXIntelligence.CommandAction.StartQuiz -> {
                            onStartQuickQuiz?.invoke(action.classLevel, action.subject, action.chapter)
                        }
                        is ThinkXIntelligence.CommandAction.OpenRevise -> {
                            onNavigateToRevise?.invoke()
                        }
                        is ThinkXIntelligence.CommandAction.OpenMistakes -> {
                            onNavigateToMistakes?.invoke()
                        }
                        is ThinkXIntelligence.CommandAction.AskAI -> {
                            onNavigateToAI?.invoke(selectedClass, selectedSubject, action.prompt)
                        }
                        is ThinkXIntelligence.CommandAction.OpenMastery -> {
                            showMasteryMap = true
                        }
                        is ThinkXIntelligence.CommandAction.OpenWeeklyReport -> {
                            showWeeklyReport = true
                        }
                        is ThinkXIntelligence.CommandAction.OpenChallenge -> {
                            showChallengeDialog = true
                        }
                        is ThinkXIntelligence.CommandAction.OpenFlashcards -> {
                            onNavigateToCards?.invoke()
                        }
                        is ThinkXIntelligence.CommandAction.Unknown -> {
                            onNavigateToAI?.invoke(selectedClass, selectedSubject, action.rawQuery)
                        }
                    }
                }
            )
        }

        // WEEKLY INTELLIGENCE REPORT DIALOG
        if (showWeeklyReport) {
            WeeklyReportDialog(
                storage = storage,
                onDismiss = { showWeeklyReport = false },
                onStartPractice = {
                    onStartQuickQuiz?.invoke(selectedClass, selectedSubject, selectedChapter)
                }
            )
        }

        // CONCEPT MASTERY MAP DIALOG
        if (showMasteryMap) {
            MasteryMapDialog(
                storage = storage,
                initialClass = selectedClass,
                initialSubject = selectedSubject,
                onDismiss = { showMasteryMap = false },
                onQuizChapter = { sub, ch ->
                    onStartQuickQuiz?.invoke(selectedClass, sub, ch)
                },
                onReviseChapter = { sub, ch ->
                    onSubjectSelected(sub)
                    onChapterSelected(ch)
                    onNavigateToRevise?.invoke()
                },
                onAskAIConcept = { concept, sub, ch ->
                    onNavigateToAI?.invoke(selectedClass, sub, "Explain $concept in $ch thoroughly with diagrams and exam key points")
                }
            )
        }

        // CHALLENGE ME DIALOG
        if (showChallengeDialog) {
            ChallengeMeDialog(
                storage = storage,
                classLevel = selectedClass,
                currentSubject = selectedSubject,
                currentChapter = selectedChapter,
                onDismiss = { showChallengeDialog = false },
                onLaunchChallenge = { challenge ->
                    if (onStartChallenge != null) {
                        onStartChallenge(challenge)
                    } else {
                        onStartQuickQuiz?.invoke(challenge.classLevel, challenge.subject, challenge.chapter)
                    }
                }
            )
        }

        // DEEP FOCUS MODE DIALOG
        if (showFocusMode) {
            FocusModeDialog(
                subject = selectedSubject,
                chapter = selectedChapter,
                onDismiss = { showFocusMode = false },
                onCompleteFocusSession = { minutes ->
                    storage.addXp(minutes * 2)
                    dailyPulse = storage.getDailyPulse()
                }
            )
        }
    }
}

@Composable
private fun IntelligenceRibbonChip(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    val palette = LocalThemePalette.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(palette.surfaceGlass)
            .border(1.dp, palette.borderGlass, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PulseMetric(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextWhite)
        Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun MetricColumn(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGrayBlue, letterSpacing = 0.5.sp)
    }
}

private fun getSubjectIcon(subject: String): ImageVector {
    return when (subject.lowercase()) {
        "science" -> Icons.Rounded.Science
        "physics" -> Icons.Rounded.Bolt
        "chemistry" -> Icons.Rounded.Biotech
        "biology" -> Icons.Rounded.Spa
        "mathematics", "math" -> Icons.Rounded.Calculate
        "social science" -> Icons.Rounded.Public
        "english", "english core" -> Icons.Rounded.AutoStories
        "hindi" -> Icons.Rounded.MenuBook
        "economics" -> Icons.Rounded.TrendingUp
        "computer science", "computers" -> Icons.Rounded.Laptop
        else -> Icons.Rounded.School
    }
}
