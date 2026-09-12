package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppStorage
import com.example.data.ThinkXIntelligence
import com.example.model.ChallengeType
import com.example.model.Difficulty
import com.example.ui.theme.*

@Composable
fun ThinkXCommandCenterDialog(
    storage: AppStorage,
    classLevel: String,
    currentSubject: String,
    currentChapter: String,
    onDismiss: () -> Unit,
    onExecuteAction: (ThinkXIntelligence.CommandAction) -> Unit
) {
    val palette = LocalThemePalette.current
    var query by remember { mutableStateOf("") }

    val presetCommands = remember {
        listOf(
            "Revise ${currentSubject}",
            "Quiz me on my mistakes",
            "Start a speed challenge",
            "Explain key concepts",
            "Show my concept mastery map",
            "View weekly intelligence report",
            "Start an exam simulation quiz"
        )
    }

    fun submitQuery(text: String) {
        if (text.isNotBlank()) {
            val action = ThinkXIntelligence.interpretCommand(
                query = text,
                classLevel = classLevel,
                currentSubject = currentSubject,
                currentChapter = currentChapter,
                storage = storage
            )
            onDismiss()
            onExecuteAction(action)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            borderGlow = true,
            cornerRadius = 24.dp,
            contentPadding = PaddingValues(18.dp),
            testTag = "command_center_dialog"
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(palette.primaryAccent.copy(alpha = 0.15f))
                                .border(1.dp, palette.primaryAccent.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Terminal,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COMMAND CENTER",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent,
                            letterSpacing = 1.5.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Input bar
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            text = "e.g. 'Revise electricity', 'Quiz mistakes'...",
                            fontSize = 13.sp,
                            color = TextGrayBlue
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            tint = palette.primaryAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (query.isNotBlank()) {
                            IconButton(onClick = { submitQuery(query) }) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowForward,
                                    contentDescription = "Execute",
                                    tint = palette.primaryAccent
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { submitQuery(query) }),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = palette.surfaceGlass,
                        unfocusedContainerColor = palette.surfaceGlass,
                        focusedBorderColor = palette.primaryAccent,
                        unfocusedBorderColor = palette.borderGlass,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "QUICK INTELLIGENCE ACTIONS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGrayBlue,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val displayList = if (query.isBlank()) {
                        presetCommands
                    } else {
                        presetCommands.filter { it.contains(query, ignoreCase = true) }.ifEmpty {
                            listOf("Ask AI: \"$query\"")
                        }
                    }

                    items(displayList) { cmd ->
                        val icon = when {
                            cmd.startsWith("Revise", ignoreCase = true) -> Icons.Rounded.MenuBook
                            cmd.startsWith("Quiz", ignoreCase = true) -> Icons.Rounded.Quiz
                            cmd.startsWith("Start a speed", ignoreCase = true) -> Icons.Rounded.FlashOn
                            cmd.startsWith("Explain", ignoreCase = true) -> Icons.Rounded.AutoAwesome
                            cmd.startsWith("Show my concept", ignoreCase = true) -> Icons.Rounded.AccountTree
                            cmd.startsWith("View weekly", ignoreCase = true) -> Icons.Rounded.Insights
                            cmd.startsWith("Start an exam", ignoreCase = true) -> Icons.Rounded.WorkspacePremium
                            else -> Icons.Rounded.Search
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(palette.surfaceGlass)
                                .border(1.dp, palette.borderGlass, RoundedCornerShape(12.dp))
                                .clickable { submitQuery(cmd.removePrefix("Ask AI: \"").removeSuffix("\"")) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = palette.primaryAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = cmd,
                                    fontSize = 13.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                                tint = TextGrayBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
