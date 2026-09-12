package com.example.ui.components

import android.Manifest
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ai.ThinkXAIService
import com.example.model.VoiceTutorMode
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

enum class VoiceState {
    IDLE, LISTENING, THINKING, SPEAKING
}

@Composable
fun VoiceAiDialog(
    onDismiss: () -> Unit,
    currentSubject: String,
    currentChapter: String,
    classLevel: String = "10",
    onTranscriptionReceived: (String) -> Unit
) {
    VoiceTutorDialog(
        onDismiss = onDismiss,
        classLevel = classLevel,
        currentSubject = currentSubject,
        currentChapter = currentChapter,
        onTranscriptionReceived = onTranscriptionReceived
    )
}

@Composable
fun VoiceTutorDialog(
    onDismiss: () -> Unit,
    currentSubject: String,
    currentChapter: String,
    classLevel: String = "10",
    onTranscriptionReceived: (String) -> Unit
) {
    val context = LocalContext.current
    val palette = LocalThemePalette.current
    val coroutineScope = rememberCoroutineScope()

    var selectedMode by remember { mutableStateOf(VoiceTutorMode.EXPLAIN) }
    var voiceState by remember { mutableStateOf(VoiceState.IDLE) }
    var statusMessage by remember { mutableStateOf("Ready to tutor on $currentChapter") }
    var spokenResponse by remember { mutableStateOf("Hi! I'm ThinkX Voice Tutor. Select a mode or tap Speak to begin interactive voice study.") }
    var isMicMuted by remember { mutableStateOf(false) }

    // Android TTS instance
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    DisposableEffect(context) {
        val ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Initialize TTS
            }
        }
        ttsInstance.language = Locale.US
        tts = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    fun speakText(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ThinkXVoice")
    }

    fun triggerModeSession(mode: VoiceTutorMode) {
        selectedMode = mode
        coroutineScope.launch {
            tts?.stop()
            voiceState = VoiceState.LISTENING
            statusMessage = "Listening to your request..."
            delay(1500)
            voiceState = VoiceState.THINKING
            statusMessage = "ThinkX AI synthesizing academic guidance..."
            delay(1000)

            val prompt = when (mode) {
                VoiceTutorMode.EXPLAIN -> "Provide a concise 2-sentence spoken summary of the core concept in $currentChapter."
                VoiceTutorMode.PRACTICE -> "Ask me 1 quick conceptual question from $currentChapter to test my understanding."
                VoiceTutorMode.RAPID_REVISION -> "Give me the 3 most critical points and exam formulas for $currentChapter."
                VoiceTutorMode.ASK_ME_QUESTIONS -> "Challenge me with a tough Board Exam question from $currentChapter."
                VoiceTutorMode.EXAM_PRACTICE -> "Give me a high-yield exam question from $currentChapter with marking criteria."
            }

            val response = ThinkXAIService.askTutor(
                question = prompt,
                subject = currentSubject,
                chapter = currentChapter
            )

            spokenResponse = response
            voiceState = VoiceState.SPEAKING
            statusMessage = "ThinkX Voice speaking:"
            speakText(response)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "voiceOrb")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = if (voiceState == VoiceState.LISTENING || voiceState == VoiceState.SPEAKING) 0.92f else 1f,
        targetValue = if (voiceState == VoiceState.LISTENING || voiceState == VoiceState.SPEAKING) 1.12f else 1.02f,
        animationSpec = infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Dialog(onDismissRequest = {
        tts?.stop()
        onDismiss()
    }) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            borderGlow = true,
            cornerRadius = 28.dp,
            contentPadding = PaddingValues(20.dp),
            testTag = "voice_tutor_dialog"
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(palette.primaryAccent.copy(alpha = 0.15f))
                                .border(1.dp, palette.primaryAccent.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.GraphicEq,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "REALTIME AI TUTOR 2.0",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.primaryAccent,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = "ThinkX Voice Tutor",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                    }
                    IconButton(onClick = {
                        tts?.stop()
                        onDismiss()
                    }) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Selector Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(VoiceTutorMode.entries) { mode ->
                        val isSel = selectedMode == mode
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) palette.primaryAccent.copy(alpha = 0.2f) else palette.surfaceGlass)
                                .border(1.dp, if (isSel) palette.primaryAccent else palette.borderGlass, RoundedCornerShape(12.dp))
                                .clickable { triggerModeSession(mode) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = mode.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) palette.primaryAccent else TextGrayBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Glowing AI Voice Orb
                Box(
                    modifier = Modifier
                        .scale(pulseScale)
                        .size(100.dp)
                        .shadow(24.dp, CircleShape, spotColor = palette.primaryAccent)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    when (voiceState) {
                                        VoiceState.SPEAKING -> SoftGreen
                                        VoiceState.LISTENING -> palette.primaryAccent
                                        VoiceState.THINKING -> palette.secondaryAccent
                                        VoiceState.IDLE -> palette.surfaceGlass
                                    },
                                    palette.surfaceGlass,
                                    Color.Transparent
                                )
                            )
                        )
                        .border(
                            2.dp,
                            when (voiceState) {
                                VoiceState.SPEAKING -> SoftGreen
                                VoiceState.LISTENING -> palette.primaryAccent
                                VoiceState.THINKING -> palette.secondaryAccent
                                VoiceState.IDLE -> palette.borderGlass
                            },
                            CircleShape
                        )
                        .clickable { triggerModeSession(selectedMode) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (voiceState) {
                            VoiceState.SPEAKING -> Icons.Rounded.VolumeUp
                            VoiceState.LISTENING -> Icons.Rounded.Mic
                            VoiceState.THINKING -> Icons.Rounded.Psychology
                            VoiceState.IDLE -> Icons.Rounded.PlayArrow
                        },
                        contentDescription = null,
                        tint = if (voiceState == VoiceState.IDLE) palette.primaryAccent else VoidBlack,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Waveform equalizer bars
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(14, 28, 44, 22, 38, 50, 26, 16).forEachIndexed { i, h ->
                        val barHeight by infiniteTransition.animateFloat(
                            initialValue = if (voiceState == VoiceState.SPEAKING || voiceState == VoiceState.LISTENING) (h * 0.3f) else 4f,
                            targetValue = if (voiceState == VoiceState.SPEAKING || voiceState == VoiceState.LISTENING) h.toFloat() else 8f,
                            animationSpec = infiniteRepeatable(
                                tween(350 + i * 70, easing = LinearEasing),
                                RepeatMode.Reverse
                            ),
                            label = "wave$i"
                        )
                        Box(
                            modifier = Modifier
                                .width(3.5.dp)
                                .height(barHeight.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    when (voiceState) {
                                        VoiceState.SPEAKING -> SoftGreen
                                        VoiceState.LISTENING -> palette.primaryAccent
                                        else -> palette.borderGlass
                                    }
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = statusMessage,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (voiceState) {
                        VoiceState.SPEAKING -> SoftGreen
                        VoiceState.LISTENING -> palette.primaryAccent
                        VoiceState.THINKING -> AmberGlow
                        VoiceState.IDLE -> TextGrayBlue
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Spoken dialogue transcript card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp, max = 110.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.borderGlass, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = spokenResponse,
                        fontSize = 12.sp,
                        color = TextWhite,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (voiceState == VoiceState.SPEAKING) {
                                tts?.stop()
                                voiceState = VoiceState.IDLE
                                statusMessage = "Voice paused"
                            } else {
                                triggerModeSession(selectedMode)
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(palette.surfaceGlass)
                            .border(1.dp, palette.borderGlass, CircleShape)
                            .testTag("voice_trigger_btn")
                    ) {
                        Icon(
                            imageVector = if (voiceState == VoiceState.SPEAKING) Icons.Rounded.Pause else Icons.Rounded.Mic,
                            contentDescription = "Trigger",
                            tint = palette.primaryAccent
                        )
                    }

                    GlassButton(
                        text = if (voiceState == VoiceState.SPEAKING) "STOP AUDIO" else "ASK VOICE TUTOR",
                        onClick = {
                            if (voiceState == VoiceState.SPEAKING) {
                                tts?.stop()
                                voiceState = VoiceState.IDLE
                            } else {
                                triggerModeSession(selectedMode)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = if (voiceState == VoiceState.SPEAKING) Icons.Rounded.Stop else Icons.Rounded.VolumeUp,
                                contentDescription = null,
                                tint = VoidBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ImageGenDialog(
    onDismiss: () -> Unit,
    currentChapter: String
) {
    val palette = LocalThemePalette.current
    var prompt by remember { mutableStateOf("Scientific diagram of $currentChapter with labeled parts") }
    var isGenerating by remember { mutableStateOf(false) }
    var isGenerated by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "shimmer"
    )

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            borderGlow = true,
            cornerRadius = 24.dp,
            contentPadding = PaddingValues(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CONCEPT VISUALIZER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "AI Generated Image",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                GlassInput(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = "Enter visual concept to generate...",
                    singleLine = false,
                    testTag = "image_gen_prompt_input"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Image Preview Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.surfaceGlass)
                        .border(1.dp, palette.borderGlass, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGenerating) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(palette.primaryAccent.copy(alpha = shimmerAlpha))
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Synthesizing diagram with ThinkX AI...",
                                fontSize = 12.sp,
                                color = palette.primaryAccent
                            )
                        }
                    } else if (isGenerated) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = SoftGreen,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "High-Yield Diagram Ready",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Diagram saved to NCERT Study Vault",
                                fontSize = 11.sp,
                                color = TextGrayBlue
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = palette.primaryAccent,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ready to generate visual study aids",
                                fontSize = 12.sp,
                                color = TextGrayBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassButton(
                    text = if (isGenerating) "GENERATING..." else "GENERATE DIAGRAM",
                    onClick = {
                        isGenerating = true
                        // Coroutine simulation
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Brush,
                            contentDescription = null,
                            tint = VoidBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    testTag = "image_gen_submit_button"
                )
            }
        }
    }
}
