package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Flashcard
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun FlashcardsScreen(
    flashcards: List<Flashcard>,
    onAddCustomCard: (Flashcard) -> Unit
) {
    val palette = LocalThemePalette.current

    val categories = listOf("All", "Science", "Physics", "Chemistry", "Biology", "AI")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredCards = remember(flashcards, selectedCategory) {
        if (selectedCategory == "All") flashcards
        else flashcards.filter { it.subject.equals(selectedCategory, ignoreCase = true) }
    }

    var cardIndex by remember(filteredCards) { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val currentCard = filteredCards.getOrNull(cardIndex.coerceIn(0, (filteredCards.size - 1).coerceAtLeast(0)))

    // 3D Flip Rotation Animation
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(400),
        label = "cardFlip"
    )

    LiquidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "STUDY AIDS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "NCERT Flashcards",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                // Add Custom Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(palette.primaryAccent.copy(alpha = 0.2f))
                        .border(1.dp, palette.primaryAccent, RoundedCornerShape(20.dp))
                        .clickable { showAddDialog = true }
                        .testTag("flashcard_add_custom_button")
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            tint = palette.primaryAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add Custom",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    GlassPill(
                        text = cat,
                        isSelected = selectedCategory == cat,
                        onClick = {
                            selectedCategory = cat
                            cardIndex = 0
                            isFlipped = false
                        },
                        testTag = "flashcard_cat_${cat.lowercase()}"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (filteredCards.isEmpty() || currentCard == null) {
                // Empty state
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(palette.surfaceGlass)
                                .border(1.dp, palette.borderGlass, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Style,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No flashcards found!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap \"Add Custom\" to create your own flashcards.",
                            fontSize = 13.sp,
                            color = TextGrayBlue,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Flashcard Counter
                Text(
                    text = "CARD ${cardIndex + 1} OF ${filteredCards.size}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryAccent,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3D Flippable Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(palette.surfaceGlass.copy(alpha = 0.88f))
                        .border(
                            width = 1.5.dp,
                            color = if (isFlipped) palette.secondaryAccent else palette.primaryAccent,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { isFlipped = !isFlipped }
                        .testTag("flashcard_flip_target")
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // Front: Concept / Question
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            GlassBadge(text = currentCard.subject.uppercase(), color = palette.primaryAccent)
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = currentCard.concept,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                textAlign = TextAlign.Center,
                                lineHeight = 28.sp
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.TouchApp,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tap to reveal answer",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    } else {
                        // Back: Answer / Explanation (flipped horizontally so text is readable)
                        Column(
                            modifier = Modifier.graphicsLayer { rotationY = 180f },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            GlassBadge(text = "EXPLANATION", color = palette.secondaryAccent)
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = currentCard.explanation,
                                fontSize = 15.sp,
                                color = TextWhite,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.TouchApp,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tap to flip back",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Navigation Controls (Previous, Next, Shuffle)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (cardIndex > 0) {
                                cardIndex -= 1
                                isFlipped = false
                            }
                        },
                        enabled = cardIndex > 0,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, CircleShape)
                            .testTag("flashcard_prev_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Previous",
                            tint = if (cardIndex > 0) TextWhite else TextMuted
                        )
                    }

                    IconButton(
                        onClick = {
                            cardIndex = (filteredCards.indices).random()
                            isFlipped = false
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, CircleShape)
                            .testTag("flashcard_shuffle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Shuffle,
                            contentDescription = "Shuffle",
                            tint = palette.primaryAccent
                        )
                    }

                    IconButton(
                        onClick = {
                            if (cardIndex < filteredCards.size - 1) {
                                cardIndex += 1
                                isFlipped = false
                            }
                        },
                        enabled = cardIndex < filteredCards.size - 1,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, CircleShape)
                            .testTag("flashcard_next_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "Next",
                            tint = if (cardIndex < filteredCards.size - 1) TextWhite else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(110.dp))
        }
    }

    // Add Custom Card Modal
    if (showAddDialog) {
        var newConcept by remember { mutableStateOf("") }
        var newExplanation by remember { mutableStateOf("") }
        var newSubject by remember { mutableStateOf("Science") }

        Dialog(onDismissRequest = { showAddDialog = false }) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                borderGlow = true,
                cornerRadius = 24.dp
            ) {
                Text(
                    text = "CREATE CUSTOM CARD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryAccent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add NCERT Concept",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Subject", fontSize = 11.sp, color = TextGrayBlue)
                Spacer(modifier = Modifier.height(4.dp))
                GlassInput(
                    value = newSubject,
                    onValueChange = { newSubject = it },
                    placeholder = "e.g. Physics, Biology, Math"
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Concept / Question (Front)", fontSize = 11.sp, color = TextGrayBlue)
                Spacer(modifier = Modifier.height(4.dp))
                GlassInput(
                    value = newConcept,
                    onValueChange = { newConcept = it },
                    placeholder = "What is the concept or question?",
                    testTag = "custom_card_concept_input"
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Answer / Explanation (Back)", fontSize = 11.sp, color = TextGrayBlue)
                Spacer(modifier = Modifier.height(4.dp))
                GlassInput(
                    value = newExplanation,
                    onValueChange = { newExplanation = it },
                    placeholder = "Detailed explanation or formula",
                    singleLine = false,
                    testTag = "custom_card_answer_input"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassButton(
                        text = "CANCEL",
                        onClick = { showAddDialog = false },
                        isSecondary = true,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "SAVE CARD",
                        onClick = {
                            if (newConcept.isNotBlank() && newExplanation.isNotBlank()) {
                                val card = Flashcard(
                                    subject = newSubject.trim(),
                                    concept = newConcept.trim(),
                                    explanation = newExplanation.trim(),
                                    isCustom = true
                                )
                                onAddCustomCard(card)
                                showAddDialog = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "custom_card_save_button"
                    )
                }
            }
        }
    }
}
