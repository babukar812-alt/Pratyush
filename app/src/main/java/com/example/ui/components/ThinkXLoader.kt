package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * ThinkX Loading System 2.0
 * Sequential 8-Stage Choreographed AI Orchestration Experience:
 * Stage 1: Atmospheric background glow slowly appears
 * Stage 2: Translucent glass container softly fades in
 * Stage 3: ThinkX TX mark appears with a subtle scale spring animation
 * Stage 4: Cyan/Purple orbital energy ring forms around the mark
 * Stage 5: Micro-particles orbit the energy core
 * Stage 6: Soft luminous light sweep across glass
 * Stage 7: Status text fades in with smooth cross-fade
 * Stage 8: Subtle linear progress indicator advances
 * Completion: Progress hits 100% -> Energy ring briefly brightens -> TX mark glows -> Soft container fade out.
 */
@Composable
fun ThinkXLoader(
    modifier: Modifier = Modifier,
    isQuizMode: Boolean = false,
    customTitle: String? = null,
    fullscreen: Boolean = true,
    onComplete: (() -> Unit)? = null
) {
    val palette = LocalThemePalette.current

    // Chronological timing clock
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var isFinishing by remember { mutableStateOf(false) }

    val statusMessages = remember(isQuizMode) {
        if (isQuizMode) {
            listOf(
                "Accessing NCERT verified syllabus...",
                "Selecting authentic textbook questions...",
                "Synthesizing dynamic options & hints...",
                "Verifying explanation accuracy...",
                "Finalizing your personalized test paper..."
            )
        } else {
            listOf(
                "Preparing your study space...",
                "Loading NCERT content...",
                "Connecting to ThinkX AI engine...",
                "Synthesizing core concepts...",
                "Almost ready..."
            )
        }
    }

    val totalDurationMs = 2600L

    LaunchedEffect(Unit) {
        val start = System.currentTimeMillis()
        while (true) {
            val diff = System.currentTimeMillis() - start
            elapsedMs = diff

            if (onComplete != null && diff >= totalDurationMs && !isFinishing) {
                isFinishing = true
                delay(350) // Brighten & soft fade-out delay
                onComplete()
                break
            }
            delay(16)
        }
    }

    // Sequence Stages
    // Stage 1: Atmospheric glow (0 - 400ms)
    val stage1GlowAlpha = (elapsedMs / 400f).coerceIn(0f, 1f)

    // Stage 2: Glass container fade (300 - 800ms)
    val stage2ContainerAlpha = ((elapsedMs - 300f) / 500f).coerceIn(0f, 1f)

    // Stage 3: TX Mark scale spring (600 - 1100ms)
    val stage3MarkFraction = ((elapsedMs - 600f) / 500f).coerceIn(0f, 1f)
    val stage3MarkScale = if (stage3MarkFraction <= 0f) 0f else (0.6f + 0.4f * stage3MarkFraction)

    // Stage 4: Energy Ring formation (900 - 1500ms)
    val stage4RingAlpha = ((elapsedMs - 900f) / 600f).coerceIn(0f, 1f)

    // Stage 5: Orbiting micro-particles (1200ms onwards)
    val stage5ParticleAlpha = ((elapsedMs - 1200f) / 500f).coerceIn(0f, 1f)

    // Stage 6: Light sweep across glass (1500 - 2300ms)
    val sweepFraction = ((elapsedMs - 1500f) / 800f).coerceIn(0f, 1f)

    // Stage 7: Status text index with smooth crossfade
    val statusIndex = when {
        elapsedMs < 600 -> 0
        elapsedMs < 1200 -> 1
        elapsedMs < 1700 -> 2
        elapsedMs < 2200 -> 3
        else -> 4
    }.coerceIn(0, statusMessages.size - 1)

    // Stage 8: Progress fraction (1000 - 2600ms)
    val progress = ((elapsedMs - 1000f) / 1500f).coerceIn(0f, 1f)

    // Infinite ambient motions
    val infiniteTransition = rememberInfiniteTransition(label = "thinkx_orbit")
    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart),
        label = "orbit_rot"
    )

    val ambientPulse by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ambient_pulse"
    )

    // Completion brightness & fade-out
    val completionBrighten by animateFloatAsState(
        targetValue = if (isFinishing) 1.6f else 1f,
        animationSpec = tween(250),
        label = "brighten"
    )
    val containerExitAlpha by animateFloatAsState(
        targetValue = if (isFinishing) 0f else 1f,
        animationSpec = tween(300),
        label = "exitAlpha"
    )

    val loaderContent = @Composable {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(if (fullscreen) 0.92f else 1f)
                .alpha(stage2ContainerAlpha * containerExitAlpha)
                .shadow(
                    elevation = (20 * stage2ContainerAlpha).dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = palette.primaryAccent.copy(alpha = 0.35f * stage1GlowAlpha),
                    spotColor = palette.glowColor1.copy(alpha = 0.6f * stage1GlowAlpha)
                )
                .clip(RoundedCornerShape(26.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            palette.surfaceGlass.copy(alpha = 0.94f * stage2ContainerAlpha),
                            palette.surfaceGlass.copy(alpha = 0.82f * stage2ContainerAlpha)
                        )
                    )
                )
                .border(
                    width = 1.2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            palette.primaryAccent.copy(alpha = 0.8f * stage2ContainerAlpha),
                            palette.secondaryAccent.copy(alpha = 0.35f * stage2ContainerAlpha)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(vertical = 32.dp, horizontal = 22.dp),
            contentAlignment = Alignment.Center
        ) {
            // Stage 6: Light sweep overlay across the container
            if (sweepFraction > 0f && sweepFraction < 1f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = (sweepFraction * 320 - 160).dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    palette.primaryAccent.copy(alpha = 0.12f * (1f - sweepFraction)),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Central Core: Stages 3, 4, 5 with orbital canvas
                Box(
                    modifier = Modifier.size(126.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerPt = Offset(size.width / 2f, size.height / 2f)
                        val radius = size.width * 0.42f

                        // Stage 4: Cyan/Purple orbital energy ring
                        if (stage4RingAlpha > 0f) {
                            drawCircle(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        palette.primaryAccent.copy(alpha = (stage4RingAlpha * 0.9f * completionBrighten).coerceAtMost(1f)),
                                        palette.secondaryAccent.copy(alpha = (stage4RingAlpha * 0.95f * completionBrighten).coerceAtMost(1f)),
                                        palette.glowColor1.copy(alpha = 0.4f * stage4RingAlpha),
                                        palette.primaryAccent.copy(alpha = (stage4RingAlpha * 0.9f * completionBrighten).coerceAtMost(1f))
                                    ),
                                    center = centerPt
                                ),
                                radius = radius,
                                center = centerPt,
                                style = Stroke(width = (3.5f * completionBrighten).coerceAtMost(7f), cap = StrokeCap.Round)
                            )
                        }

                        // Stage 5: Orbiting micro-particles
                        if (stage5ParticleAlpha > 0f) {
                            for (i in 0 until 6) {
                                val angle = (orbitRotation * (1f + i * 0.2f) + (i * 60f)) * (Math.PI / 180f)
                                val dist = radius * (0.88f + 0.18f * sin((orbitRotation * 0.05f + i).toDouble()).toFloat())
                                val px = centerPt.x + (dist * cos(angle)).toFloat()
                                val py = centerPt.y + (dist * sin(angle)).toFloat()
                                val pColor = if (i % 2 == 0) palette.primaryAccent else palette.secondaryAccent
                                drawCircle(
                                    color = pColor.copy(alpha = (0.75f * stage5ParticleAlpha * completionBrighten).coerceAtMost(1f)),
                                    radius = 2.8f,
                                    center = Offset(px, py)
                                )
                            }
                        }
                    }

                    // Stage 3: ThinkX TX Mark in frosted glass orb with ambient pulse
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .scale(stage3MarkScale * ambientPulse)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        palette.primaryAccent.copy(alpha = (0.35f * completionBrighten).coerceAtMost(0.8f)),
                                        palette.surfaceGlass.copy(alpha = 0.96f)
                                    )
                                )
                            )
                            .border(
                                width = 1.2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        palette.primaryAccent.copy(alpha = (0.75f * completionBrighten).coerceAtMost(1f)),
                                        palette.secondaryAccent.copy(alpha = 0.45f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "TX",
                            color = TextWhite,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Title
                Text(
                    text = customTitle ?: (if (isQuizMode) "Generating NCERT Quiz" else "ThinkX AI Engine"),
                    color = TextWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stage 7: Status text with smooth animated cross-fade
                Box(
                    modifier = Modifier
                        .height(26.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = statusMessages.getOrElse(statusIndex) { "Loading..." },
                        transitionSpec = {
                            fadeIn(tween(350)) togetherWith fadeOut(tween(250))
                        },
                        label = "status_crossfade"
                    ) { message ->
                        Text(
                            text = message,
                            color = palette.primaryAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isQuizMode) {
                    // Staged verification checklist
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        val stages = listOf(
                            "NCERT Syllabus Access",
                            "Textbook Questions Selected",
                            "Adaptive Options Generated",
                            "Explanation Verification"
                        )
                        stages.forEachIndexed { idx, stageLabel ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stageLabel,
                                    fontSize = 12.sp,
                                    color = if (idx <= statusIndex) TextWhite else TextMuted
                                )
                                when {
                                    idx < statusIndex -> {
                                        Text(text = "✓", color = palette.primaryAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                    idx == statusIndex -> {
                                        Text(text = "●", color = palette.secondaryAccent, fontSize = 12.sp)
                                    }
                                    else -> {
                                        Text(text = "○", color = TextMuted, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Stage 8: Smooth linear progress indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(palette.borderGlass)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(palette.primaryAccent, palette.secondaryAccent)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }

    if (fullscreen) {
        LiquidBackground {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Stage 1: Ambient background radial glow
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                palette.primaryAccent.copy(alpha = 0.22f * stage1GlowAlpha * completionBrighten),
                                palette.glowColor1.copy(alpha = 0.12f * stage1GlowAlpha),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.minDimension * 0.65f
                        )
                    )
                }

                loaderContent()
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            loaderContent()
        }
    }
}
