package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Animated liquid glass background with floating nebula glow orbs and subtle cosmic particles.
 */
@Composable
fun LiquidBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val palette = LocalThemePalette.current

    // Infinite gentle cosmic drift
    val infiniteTransition = rememberInfiniteTransition(label = "LiquidNebula")
    val animOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animOffset1"
    )
    val animOffset2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animOffset2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.backgroundBase)
            .drawBehind {
                val w = size.width
                val h = size.height

                // 1. Base deep mesh radial gradient
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(palette.glowColor1.copy(alpha = 0.22f), Color.Transparent),
                        center = Offset(w * (0.25f + 0.5f * animOffset1), h * (0.2f + 0.15f * animOffset2)),
                        radius = w * 0.75f
                    ),
                    radius = w * 0.75f,
                    center = Offset(w * (0.25f + 0.5f * animOffset1), h * (0.2f + 0.15f * animOffset2))
                )

                // 2. Violet / Accent 2 glow orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(palette.glowColor2.copy(alpha = 0.20f), Color.Transparent),
                        center = Offset(w * (0.8f - 0.4f * animOffset2), h * (0.65f + 0.2f * animOffset1)),
                        radius = w * 0.8f
                    ),
                    radius = w * 0.8f,
                    center = Offset(w * (0.8f - 0.4f * animOffset2), h * (0.65f + 0.2f * animOffset1))
                )

                // 3. Central ambient depth pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(palette.primaryAccent.copy(alpha = 0.10f), Color.Transparent),
                        center = Offset(w * 0.5f, h * 0.45f),
                        radius = w * 0.55f
                    ),
                    radius = w * 0.55f,
                    center = Offset(w * 0.5f, h * 0.45f)
                )

                // 4. Floating subtle stars / particles
                val starPositions = listOf(
                    Offset(w * 0.12f, h * 0.15f),
                    Offset(w * 0.85f, h * 0.18f),
                    Offset(w * 0.42f, h * 0.28f),
                    Offset(w * 0.75f, h * 0.42f),
                    Offset(w * 0.22f, h * 0.58f),
                    Offset(w * 0.88f, h * 0.78f),
                    Offset(w * 0.35f, h * 0.85f)
                )
                starPositions.forEachIndexed { i, pt ->
                    val starAlpha = 0.3f + 0.4f * ((animOffset1 + i * 0.14f) % 1f)
                    drawCircle(
                        color = Color.White.copy(alpha = starAlpha),
                        radius = if (i % 2 == 0) 2.2f else 1.5f,
                        center = pt
                    )
                }
            },
        content = content
    )
}

/**
 * Reusable Liquid Glass Card with translucent blur styling, subtle borders, and soft shadows.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderGlow: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    testTag: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val palette = LocalThemePalette.current
    val shape = RoundedCornerShape(cornerRadius)

    val borderColor = if (borderGlow) palette.primaryAccent.copy(alpha = 0.6f) else palette.borderGlass

    var cardModifier = modifier
        .shadow(
            elevation = if (borderGlow) 10.dp else 4.dp,
            shape = shape,
            ambientColor = palette.primaryAccent.copy(alpha = 0.15f),
            spotColor = palette.glowColor1
        )
        .clip(shape)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    palette.surfaceGlass.copy(alpha = 0.75f),
                    palette.surfaceGlass.copy(alpha = 0.50f)
                )
            )
        )
        .border(
            width = if (borderGlow) 1.5.dp else 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    borderColor,
                    borderColor.copy(alpha = 0.15f)
                )
            ),
            shape = shape
        )

    if (onClick != null) {
        cardModifier = cardModifier.clickable { onClick() }
    }

    if (testTag != null) {
        cardModifier = cardModifier.testTag(testTag)
    }

    Column(
        modifier = cardModifier.padding(contentPadding),
        content = content
    )
}

/**
 * Premium Liquid Glass Button with vibrant gradient and ripple feedback.
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    testTag: String? = null
) {
    val palette = LocalThemePalette.current
    val shape = RoundedCornerShape(16.dp)

    val backgroundBrush = if (isSecondary) {
        Brush.horizontalGradient(
            colors = listOf(
                palette.surfaceGlass.copy(alpha = 0.85f),
                palette.surfaceGlass.copy(alpha = 0.65f)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                palette.primaryAccent,
                palette.secondaryAccent
            )
        )
    }

    val borderBrush = if (isSecondary) {
        Brush.horizontalGradient(
            colors = listOf(
                palette.borderGlass,
                palette.primaryAccent.copy(alpha = 0.3f)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.4f),
                Color.Transparent
            )
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "glassBtnScale"
    )

    var btnModifier = modifier
        .scale(buttonScale)
        .heightIn(min = 48.dp)
        .fillMaxWidth()
        .shadow(
            elevation = if (isSecondary) 2.dp else (if (isPressed) 4.dp else 8.dp),
            shape = shape,
            ambientColor = palette.primaryAccent.copy(alpha = 0.35f)
        )
        .clip(shape)
        .background(backgroundBrush)
        .border(width = 1.dp, brush = borderBrush, shape = shape)
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onClick() }
        .padding(horizontal = 20.dp, vertical = 14.dp)

    if (testTag != null) {
        btnModifier = btnModifier.testTag(testTag)
    }

    Row(
        modifier = btnModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = if (isSecondary) TextWhite else VoidBlack,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Translucent Glass Input Box
 */
@Composable
fun GlassInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    testTag: String? = null
) {
    val palette = LocalThemePalette.current
    val shape = RoundedCornerShape(16.dp)

    var rootModifier = modifier
        .fillMaxWidth()
        .clip(shape)
        .background(palette.surfaceGlass.copy(alpha = 0.7f))
        .border(
            width = 1.dp,
            color = if (value.isNotEmpty()) palette.primaryAccent.copy(alpha = 0.5f) else palette.borderGlass,
            shape = shape
        )
        .padding(horizontal = 16.dp, vertical = 14.dp)

    if (testTag != null) {
        rootModifier = rootModifier.testTag(testTag)
    }

    Row(
        modifier = rootModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(10.dp))
        }
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(palette.primaryAccent),
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(10.dp))
            trailingIcon()
        }
    }
}

/**
 * Filter / Selection Pill
 */
@Composable
fun GlassPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    testTag: String? = null
) {
    val palette = LocalThemePalette.current
    val shape = RoundedCornerShape(24.dp)

    val background = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(
                palette.primaryAccent.copy(alpha = 0.25f),
                palette.secondaryAccent.copy(alpha = 0.25f)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                palette.surfaceGlass.copy(alpha = 0.5f),
                palette.surfaceGlass.copy(alpha = 0.3f)
            )
        )
    }

    val border = if (isSelected) {
        palette.primaryAccent
    } else {
        palette.borderGlass
    }

    var pillModifier = modifier
        .heightIn(min = 36.dp)
        .clip(shape)
        .background(background)
        .border(1.dp, border, shape)
        .clickable { onClick() }
        .padding(horizontal = 14.dp, vertical = 8.dp)

    if (testTag != null) {
        pillModifier = pillModifier.testTag(testTag)
    }

    Row(
        modifier = pillModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            color = if (isSelected) palette.primaryAccent else TextGrayBlue,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

/**
 * Category/Stat Badge
 */
@Composable
fun GlassBadge(
    text: String,
    color: Color = LocalThemePalette.current.primaryAccent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Circular Glowing Avatar
 */
@Composable
fun GlassAvatar(
    initials: String,
    size: Dp = 44.dp,
    modifier: Modifier = Modifier
) {
    val palette = LocalThemePalette.current
    Box(
        modifier = modifier
            .size(size)
            .shadow(6.dp, CircleShape, spotColor = palette.primaryAccent)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        palette.primaryAccent,
                        palette.secondaryAccent
                    )
                )
            )
            .border(1.5.dp, Color.White.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = VoidBlack,
            fontSize = (size.value * 0.38f).sp,
            fontWeight = FontWeight.Black
        )
    }
}

/**
 * Smooth Glass Progress Bar
 */
@Composable
fun GlassProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp
) {
    val palette = LocalThemePalette.current
    val coercedProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(palette.surfaceGlass)
            .border(1.dp, palette.borderGlass, RoundedCornerShape(height / 2))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(coercedProgress)
                .clip(RoundedCornerShape(height / 2))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            palette.primaryAccent,
                            palette.secondaryAccent
                        )
                    )
                )
        )
    }
}
