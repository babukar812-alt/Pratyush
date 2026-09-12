package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val palette = LocalThemePalette.current

    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "logoScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "logoAlpha"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1600)
        onSplashFinished()
    }

    LiquidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .scale(scale)
                    .size(110.dp)
                    .shadow(
                        elevation = (24 * glowPulse).dp,
                        shape = CircleShape,
                        ambientColor = palette.primaryAccent,
                        spotColor = palette.secondaryAccent
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                palette.primaryAccent.copy(alpha = 0.35f),
                                palette.surfaceGlass.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                palette.primaryAccent,
                                palette.secondaryAccent,
                                palette.primaryAccent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TX",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "THINKX",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                letterSpacing = 4.sp,
                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "FUTURE OF NCERT MASTERY",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = palette.primaryAccent.copy(alpha = alpha),
                letterSpacing = 3.sp
            )
        }
    }
}
