package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FocusModeDialog(
    subject: String,
    chapter: String,
    onDismiss: () -> Unit,
    onCompleteFocusSession: (Int) -> Unit
) {
    val palette = LocalThemePalette.current

    var selectedMinutes by remember { mutableIntStateOf(25) }
    var secondsRemaining by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, secondsRemaining) {
        if (isRunning && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
            if (secondsRemaining == 0) {
                isRunning = false
                isCompleted = true
                onCompleteFocusSession(selectedMinutes)
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "focusPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(3000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            borderGlow = true,
            cornerRadius = 28.dp,
            contentPadding = PaddingValues(24.dp),
            testTag = "focus_mode_dialog"
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
                        Icon(
                            imageVector = Icons.Rounded.SelfImprovement,
                            contentDescription = null,
                            tint = palette.primaryAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DEEP FOCUS MODE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.primaryAccent,
                            letterSpacing = 1.5.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = chapter,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1
                )
                Text(
                    text = "$subject • Distraction-free NCERT Mastery",
                    fontSize = 12.sp,
                    color = TextGrayBlue
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Pulsing Focus Orb & Timer
                Box(
                    modifier = Modifier
                        .scale(if (isRunning) pulseScale else 1f)
                        .size(170.dp)
                        .shadow(28.dp, CircleShape, spotColor = palette.primaryAccent.copy(alpha = 0.4f))
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    palette.primaryAccent.copy(alpha = 0.28f),
                                    palette.secondaryAccent.copy(alpha = 0.15f),
                                    palette.surfaceGlass
                                )
                            )
                        )
                        .border(1.5.dp, palette.primaryAccent.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formattedTime,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = TextWhite
                        )
                        Text(
                            text = if (isRunning) "FOCUSING" else if (isCompleted) "COMPLETED!" else "READY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) SoftGreen else palette.primaryAccent,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Time duration selector (when not running)
                if (!isRunning && !isCompleted) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(15, 25, 45).forEach { mins ->
                            val isSel = selectedMinutes == mins
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) palette.primaryAccent.copy(alpha = 0.2f) else palette.surfaceGlass)
                                    .border(1.dp, if (isSel) palette.primaryAccent else palette.borderGlass, RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedMinutes = mins
                                        secondsRemaining = mins * 60
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "$mins min",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) palette.primaryAccent else TextWhite
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                if (isCompleted) {
                    Text(
                        text = "Great study session! +${selectedMinutes * 2} XP Earned",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftGreen
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    GlassButton(
                        text = "RETURN TO STUDY ENGINE",
                        onClick = onDismiss
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (isRunning) {
                            GlassButton(
                                text = "PAUSE",
                                onClick = { isRunning = false },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            GlassButton(
                                text = if (secondsRemaining < selectedMinutes * 60) "RESUME" else "START FOCUS",
                                onClick = { isRunning = true },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (secondsRemaining < selectedMinutes * 60) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(palette.surfaceGlass)
                                    .border(1.dp, palette.borderGlass, RoundedCornerShape(14.dp))
                                    .clickable {
                                        isRunning = false
                                        secondsRemaining = selectedMinutes * 60
                                    }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Reset", fontSize = 12.sp, color = TextGrayBlue)
                            }
                        }
                    }
                }
            }
        }
    }
}
