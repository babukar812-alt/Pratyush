package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemePalette
import com.example.ui.theme.TextMuted

enum class AppDestination(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Rounded.Home),
    REVISE("Revise", Icons.Rounded.MenuBook),
    CARDS("Cards", Icons.Rounded.Style),
    AI("AI", Icons.Rounded.AutoAwesome),
    BADGES("Badges", Icons.Rounded.EmojiEvents),
    PROFILE("Profile", Icons.Rounded.Person)
}

@Composable
fun GlassBottomDock(
    currentDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalThemePalette.current
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = shape,
                    ambientColor = palette.primaryAccent.copy(alpha = 0.25f),
                    spotColor = palette.glowColor1
                )
                .clip(shape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            palette.surfaceGlass.copy(alpha = 0.92f),
                            palette.surfaceGlass.copy(alpha = 0.82f)
                        )
                    )
                )
                .border(
                    width = 1.2.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            palette.borderGlass,
                            palette.primaryAccent.copy(alpha = 0.4f),
                            palette.secondaryAccent.copy(alpha = 0.3f),
                            palette.borderGlass
                        )
                    ),
                    shape = shape
                )
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppDestination.entries.forEach { dest ->
                val isSelected = currentDestination == dest
                val activeBgColor by animateColorAsState(
                    targetValue = if (isSelected) palette.primaryAccent.copy(alpha = 0.2f) else Color.Transparent,
                    animationSpec = tween(250),
                    label = "dockBg"
                )
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) palette.primaryAccent else TextMuted,
                    animationSpec = tween(250),
                    label = "dockIcon"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(activeBgColor)
                        .clickable { onDestinationSelected(dest) }
                        .testTag("nav_tab_${dest.name.lowercase()}")
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = dest.label,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = dest.label,
                        color = iconColor,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }
    }
}
