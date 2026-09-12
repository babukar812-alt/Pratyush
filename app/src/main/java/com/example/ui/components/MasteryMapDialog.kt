package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppStorage
import com.example.data.CurriculumData
import com.example.data.ThinkXIntelligence
import com.example.model.ConceptMasteryState
import com.example.ui.theme.*

@Composable
fun MasteryMapDialog(
    storage: AppStorage,
    initialClass: String,
    initialSubject: String,
    onDismiss: () -> Unit,
    onQuizChapter: (String, String) -> Unit,
    onReviseChapter: (String, String) -> Unit,
    onAskAIConcept: (String, String, String) -> Unit
) {
    val palette = LocalThemePalette.current
    var selectedClass by remember { mutableStateOf(initialClass) }
    var selectedSubject by remember { mutableStateOf(initialSubject) }

    val classes = remember { CurriculumData.getClasses() }
    val subjects = remember(selectedClass) { CurriculumData.getSubjects(selectedClass) }

    val masteryItems = remember(selectedClass, selectedSubject) {
        ThinkXIntelligence.getConceptMasteryMap(selectedClass, selectedSubject, storage)
    }

    val overallSubjectMastery = remember(masteryItems) {
        if (masteryItems.isNotEmpty()) {
            masteryItems.map { it.masteryPercent }.average().toInt()
        } else 0
    }

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f),
            borderGlow = true,
            cornerRadius = 28.dp,
            contentPadding = PaddingValues(16.dp),
            testTag = "mastery_map_dialog"
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
                                .background(palette.secondaryAccent.copy(alpha = 0.15f))
                                .border(1.dp, palette.secondaryAccent.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AccountTree,
                                contentDescription = null,
                                tint = palette.secondaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "DIAGNOSTIC ENGINE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.secondaryAccent,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = "Concept Mastery Map",
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

                Spacer(modifier = Modifier.height(12.dp))

                // Class selector row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(classes) { cls ->
                        val isSel = selectedClass == cls
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) palette.primaryAccent.copy(alpha = 0.2f) else palette.surfaceGlass)
                                .border(1.dp, if (isSel) palette.primaryAccent else palette.borderGlass, RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedClass = cls
                                    if (!CurriculumData.isSubjectValidForClass(cls, selectedSubject)) {
                                        selectedSubject = CurriculumData.getDefaultSubject(cls)
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Class $cls",
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) palette.primaryAccent else TextGrayBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Subject selector row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(subjects) { sub ->
                        val isSel = selectedSubject.equals(sub, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) palette.secondaryAccent.copy(alpha = 0.2f) else palette.surfaceGlass)
                                .border(1.dp, if (isSel) palette.secondaryAccent else palette.borderGlass, RoundedCornerShape(12.dp))
                            .clickable { selectedSubject = sub }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = sub,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) palette.secondaryAccent else TextGrayBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Subject Overall Mastery Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.borderGlass, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$selectedSubject Mastery Level",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "$overallSubjectMastery%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = if (overallSubjectMastery >= 75) SoftGreen else palette.primaryAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { overallSubjectMastery / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (overallSubjectMastery >= 75) SoftGreen else palette.primaryAccent,
                            trackColor = palette.surfaceGlass
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Group by chapter
                val groupedByChapter = remember(masteryItems) {
                    masteryItems.groupBy { it.chapterName }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groupedByChapter.forEach { (chapterName, itemsInChap) ->
                        val chapAvg = itemsInChap.map { it.masteryPercent }.average().toInt()
                        val totalActiveMistakes = itemsInChap.sumOf { it.mistakesCount }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(palette.surfaceGlass)
                                    .border(1.dp, palette.borderGlass, RoundedCornerShape(18.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = chapterName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "${itemsInChap.size} concepts • $chapAvg% mastered",
                                                fontSize = 11.sp,
                                                color = TextGrayBlue
                                            )
                                        }

                                        val statusLabel = when {
                                            chapAvg >= 85 -> "Mastered"
                                            chapAvg >= 70 -> "Strong"
                                            chapAvg >= 50 -> "Developing"
                                            chapAvg > 0 -> "Learning"
                                            else -> "Not Started"
                                        }
                                        val statusColor = when (statusLabel) {
                                            "Mastered" -> SoftGreen
                                            "Strong" -> palette.primaryAccent
                                            "Developing" -> AmberGlow
                                            "Learning" -> palette.secondaryAccent
                                            else -> TextGrayBlue
                                        }
                                        GlassBadge(text = statusLabel, color = statusColor)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Key concepts pill chips
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        itemsInChap.forEach { cItem ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color.White.copy(alpha = 0.03f))
                                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier.weight(1f),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(6.dp)
                                                            .clip(CircleShape)
                                                            .background(
                                                                when (cItem.state) {
                                                                    ConceptMasteryState.MASTERED -> SoftGreen
                                                                    ConceptMasteryState.STRONG -> palette.primaryAccent
                                                                    ConceptMasteryState.DEVELOPING -> AmberGlow
                                                                    else -> TextGrayBlue
                                                                }
                                                            )
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = cItem.conceptName,
                                                        fontSize = 12.sp,
                                                        color = TextWhite.copy(alpha = 0.9f)
                                                    )
                                                }

                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = "${cItem.masteryPercent}%",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = palette.primaryAccent
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    IconButton(
                                                        onClick = {
                                                            onDismiss()
                                                            onAskAIConcept(cItem.conceptName, selectedSubject, chapterName)
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.AutoAwesome,
                                                            contentDescription = "Explain with AI",
                                                            tint = palette.primaryAccent,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Action buttons for this chapter
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(palette.primaryAccent.copy(alpha = 0.15f))
                                                .border(1.dp, palette.primaryAccent.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                                .clickable {
                                                    onDismiss()
                                                    onQuizChapter(selectedSubject, chapterName)
                                                }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Test Chapter",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = palette.primaryAccent
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(palette.surfaceGlass)
                                                .border(1.dp, palette.borderGlass, RoundedCornerShape(10.dp))
                                                .clickable {
                                                    onDismiss()
                                                    onReviseChapter(selectedSubject, chapterName)
                                                }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Revise Notes",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
