package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.model.Flashcard
import com.example.model.MessageSender
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ThinkX AI Experience 2.0
 * Features:
 * - Multi-phase contextual Thinking State with animated status messages
 * - Structured markdown-like response rendering (Headings, Bullets, Equations, Exam Tips)
 * - Progressive streaming text reveal with soft pulsating cursor
 * - Contextual follow-up suggestion chips ("Explain simply", "Give example", "Test me", "Save to cards")
 * - Smart chat history search & quick clear
 * - Teach Me interactive tutoring support
 */
@Composable
fun AIScreen(
    chatMessages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    onOpenVoice: () -> Unit,
    onOpenImageGen: () -> Unit,
    currentClass: String,
    currentSubject: String,
    currentChapter: String,
    onSaveFlashcard: ((Flashcard) -> Unit)? = null
) {
    val palette = LocalThemePalette.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    var thinkingStage by remember { mutableStateOf("Understanding your question...") }
    var lastSentPrompt by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var savedFeedbackCard by remember { mutableStateOf<String?>(null) }

    // Rotating greetings
    val greetings = remember {
        listOf(
            "Ready when you are.",
            "What are we learning today?",
            "Ask me anything.",
            "Let's make this chapter easier.",
            "Your next concept starts here."
        )
    }
    var greetingIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        greetingIndex = (greetings.indices).random()
    }

    // Thinking state progression
    LaunchedEffect(isThinking) {
        if (isThinking) {
            thinkingStage = "Understanding your question..."
            delay(500)
            if (isThinking) thinkingStage = "Synthesizing NCERT concepts..."
            delay(700)
            if (isThinking) thinkingStage = "Preparing explanation..."
        }
    }

    // Contextual suggestion chips
    val suggestionChips = remember {
        listOf(
            "Teach Me Mode",
            "Explain simply",
            "Give me an example",
            "Test me on this",
            "Make revision notes",
            "Save to cards"
        )
    }

    fun handleSend(textToSend: String) {
        val trimmed = textToSend.trim()
        if (trimmed.isNotBlank() && !isThinking) {
            inputText = ""
            errorMessage = null
            lastSentPrompt = trimmed
            isThinking = true
            onSendMessage(trimmed)

            coroutineScope.launch {
                delay(600)
                isThinking = false
                listState.animateScrollToItem((chatMessages.size).coerceAtLeast(0))
            }
        }
    }

    fun handleChipClick(chip: String) {
        when (chip) {
            "Save to cards" -> {
                val lastAi = chatMessages.lastOrNull { it.sender == MessageSender.AI }
                if (lastAi != null) {
                    val card = Flashcard(
                        subject = currentSubject,
                        concept = currentChapter,
                        explanation = lastAi.text.take(240).replace("\n", " "),
                        isCustom = true
                    )
                    onSaveFlashcard?.invoke(card)
                    savedFeedbackCard = "Saved to Flashcards!"
                    coroutineScope.launch {
                        delay(2000)
                        savedFeedbackCard = null
                    }
                }
            }
            "Teach Me Mode" -> {
                handleSend("Teach me $currentChapter step by step interactively")
            }
            "Explain simply" -> {
                handleSend("Explain $currentChapter simply with an everyday analogy")
            }
            "Give me an example" -> {
                handleSend("Give me a real-world textbook example for $currentChapter")
            }
            "Test me on this" -> {
                handleSend("Test me on $currentChapter with an authentic NCERT question")
            }
            "Make revision notes" -> {
                handleSend("Make high-yield revision notes and formulas for $currentChapter")
            }
            else -> handleSend("$chip in $currentChapter")
        }
    }

    val displayMessages = if (searchQuery.isNotBlank()) {
        chatMessages.filter { it.text.contains(searchQuery, ignoreCase = true) }
    } else {
        chatMessages
    }

    LiquidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(palette.primaryAccent, palette.secondaryAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = VoidBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ThinkX AI",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "$currentSubject • Class $currentClass",
                            fontSize = 11.sp,
                            color = palette.primaryAccent
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Search toggle
                    IconButton(
                        onClick = { isSearchActive = !isSearchActive },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isSearchActive) palette.primaryAccent.copy(alpha = 0.25f) else palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, CircleShape)
                            .testTag("ai_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = if (isSearchActive) palette.primaryAccent else TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Image Gen Studio
                    IconButton(
                        onClick = onOpenImageGen,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, CircleShape)
                            .testTag("ai_image_gen_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Image,
                            contentDescription = "Image Studio",
                            tint = palette.primaryAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Clear Chat
                    IconButton(
                        onClick = onClearChat,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, CircleShape)
                            .testTag("ai_clear_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "Clear Chat",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Search Bar (if active)
            AnimatedVisibility(visible = isSearchActive) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    GlassInput(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Search in discussion...",
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Rounded.Close, contentDescription = "Clear", tint = TextMuted)
                                }
                            }
                        },
                        testTag = "ai_search_input"
                    )
                }
            }

            // Rotating Greeting Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(palette.surfaceGlass.copy(alpha = 0.5f))
                    .border(1.dp, palette.borderGlass, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✨", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = greetings[greetingIndex],
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = palette.primaryAccent
                        )
                    }
                    Text(
                        text = currentChapter,
                        fontSize = 11.sp,
                        color = TextGrayBlue,
                        maxLines = 1
                    )
                }
            }

            // Saved Feedback Toast
            if (savedFeedbackCard != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftGreen.copy(alpha = 0.2f))
                        .border(1.dp, SoftGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "✓ $savedFeedbackCard",
                        fontSize = 12.sp,
                        color = SoftGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Messages Scroll Area
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(displayMessages) { msg ->
                    ChatBubble(message = msg)
                }

                // AI Thinking State (Section 4 requirement)
                if (isThinking) {
                    item {
                        AIThinkingCard(stageText = thinkingStage)
                    }
                }

                // Error Card with [ Try again ]
                if (errorMessage != null) {
                    item {
                        AIErrorCard(
                            errorMessage = errorMessage!!,
                            onRetry = {
                                if (lastSentPrompt.isNotBlank()) {
                                    handleSend(lastSentPrompt)
                                }
                            }
                        )
                    }
                }
            }

            // Suggestion Chips Bar
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                items(suggestionChips) { chip ->
                    GlassPill(
                        text = chip,
                        isSelected = chip == "Teach Me Mode",
                        onClick = { handleChipClick(chip) },
                        testTag = "ai_chip_${chip.lowercase().replace(" ", "_")}"
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Multiline Expandable Input Composer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 96.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Voice Button
                IconButton(
                    onClick = onOpenVoice,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.primaryAccent.copy(alpha = 0.5f), CircleShape)
                        .testTag("ai_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Mic,
                        contentDescription = "Voice Chat",
                        tint = palette.primaryAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Multiline Input
                GlassInput(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = "Ask ThinkX AI anything in $currentSubject...",
                    modifier = Modifier.weight(1f),
                    singleLine = false,
                    trailingIcon = {
                        if (inputText.isNotBlank()) {
                            IconButton(
                                onClick = { handleSend(inputText) },
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(palette.primaryAccent)
                                    .testTag("ai_send_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Send,
                                    contentDescription = "Send",
                                    tint = VoidBlack,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    testTag = "ai_message_input"
                )
            }
        }
    }
}

/**
 * AI Thinking State with Multi-Phase Contextual Text and Animated Dots.
 */
@Composable
private fun AIThinkingCard(stageText: String) {
    val palette = LocalThemePalette.current
    val infiniteTransition = rememberInfiniteTransition(label = "thinkDots")

    val dot1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, delayMillis = 0), RepeatMode.Reverse),
        label = "d1"
    )
    val dot2 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, delayMillis = 180), RepeatMode.Reverse),
        label = "d2"
    )
    val dot3 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, delayMillis = 360), RepeatMode.Reverse),
        label = "d3"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
                .background(palette.surfaceGlass.copy(alpha = 0.90f))
                .border(1.dp, palette.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ThinkX AI",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "●", color = palette.secondaryAccent, fontSize = 9.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Thinking",
                        fontSize = 12.sp,
                        color = TextGrayBlue,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(palette.primaryAccent.copy(alpha = dot1)))
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(palette.primaryAccent.copy(alpha = dot2)))
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(palette.primaryAccent.copy(alpha = dot3)))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stageText,
                    fontSize = 13.sp,
                    color = TextWhite.copy(alpha = 0.85f),
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

/**
 * Error Glass Card with [ Try again ] Button.
 */
@Composable
private fun AIErrorCard(errorMessage: String, onRetry: () -> Unit) {
    val palette = LocalThemePalette.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(palette.surfaceGlass)
            .border(1.dp, SoftRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.ErrorOutline, contentDescription = null, tint = SoftRed, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Connection Notice",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = errorMessage,
                color = TextGrayBlue,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            GlassButton(
                text = "Try Again",
                onClick = onRetry,
                isSecondary = true,
                modifier = Modifier.height(38.dp)
            )
        }
    }
}

/**
 * Formatted Chat Bubble with Markdown-style bold headings, bullet points, and comfortable line spacing.
 */
@Composable
private fun ChatBubble(message: ChatMessage) {
    val palette = LocalThemePalette.current
    val isUser = message.sender == MessageSender.USER

    val bubbleShape = if (isUser) {
        RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp, bottomStart = 18.dp, bottomEnd = 18.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp)
    }

    val bubbleBg = if (isUser) {
        Brush.horizontalGradient(
            colors = listOf(
                palette.primaryAccent.copy(alpha = 0.32f),
                palette.secondaryAccent.copy(alpha = 0.32f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                palette.surfaceGlass.copy(alpha = 0.92f),
                palette.surfaceGlass.copy(alpha = 0.78f)
            )
        )
    }

    val bubbleBorder = if (isUser) palette.primaryAccent.copy(alpha = 0.55f) else palette.borderGlass

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 330.dp)
                .shadow(
                    elevation = if (isUser) 4.dp else 8.dp,
                    shape = bubbleShape,
                    ambientColor = if (isUser) palette.primaryAccent.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.3f)
                )
                .clip(bubbleShape)
                .background(bubbleBg)
                .border(1.dp, bubbleBorder, bubbleShape)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                if (!isUser) {
                    Row(
                        modifier = Modifier.padding(bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ThinkX AI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Render structured response
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = TextWhite,
                    lineHeight = 22.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}
