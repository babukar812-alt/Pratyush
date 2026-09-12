package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    onAuthenticated: (UserProfile) -> Unit
) {
    val palette = LocalThemePalette.current
    val scrollState = rememberScrollState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("10") }
    var isRegisterMode by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LiquidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo Header
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .shadow(12.dp, CircleShape, spotColor = palette.primaryAccent)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                palette.primaryAccent.copy(alpha = 0.35f),
                                palette.surfaceGlass
                            )
                        )
                    )
                    .border(1.5.dp, palette.primaryAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TX",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "THINKX",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryAccent,
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isRegisterMode) "Create Account" else "Welcome Back",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sign in to track scores & rank on leaderboards",
                fontSize = 13.sp,
                color = TextGrayBlue
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Glass Card Container
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlow = true,
                cornerRadius = 24.dp
            ) {
                Text(
                    text = "USERNAME",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryAccent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                GlassInput(
                    value = username,
                    onValueChange = { username = it; errorMessage = null },
                    placeholder = "Enter username (e.g. Aryan, Riya)",
                    leadingIcon = {
                        Icon(Icons.Rounded.Person, contentDescription = null, tint = palette.primaryAccent, modifier = Modifier.size(18.dp))
                    },
                    testTag = "auth_username_input"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryAccent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                GlassInput(
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    placeholder = "Enter password",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = {
                        Icon(Icons.Rounded.Lock, contentDescription = null, tint = palette.primaryAccent, modifier = Modifier.size(18.dp))
                    },
                    testTag = "auth_password_input"
                )

                if (isRegisterMode) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "SELECT NCERT GRADE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val ncertClasses = listOf("6", "7", "8", "9", "10", "11", "12")
                    androidx.compose.foundation.lazy.LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ncertClasses.size) { idx ->
                            val grade = ncertClasses[idx]
                            GlassPill(
                                text = "Class $grade",
                                isSelected = selectedClass == grade,
                                onClick = { selectedClass = grade }
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = SoftRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Action Button
                GlassButton(
                    text = if (isRegisterMode) "CREATE PROFILE" else "LOGIN",
                    onClick = {
                        if (username.isBlank()) {
                            errorMessage = "Please enter a username"
                        } else {
                            val initials = username.trim().take(2).uppercase()
                            val profile = UserProfile(
                                username = username.trim(),
                                avatarInitials = if (initials.isNotEmpty()) initials else "TX",
                                classLevel = selectedClass
                            )
                            onAuthenticated(profile)
                        }
                    },
                    testTag = "auth_submit_button"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle Register / Login
                GlassButton(
                    text = if (isRegisterMode) "ALREADY HAVE AN ACCOUNT? LOGIN" else "REGISTER ACCOUNT",
                    onClick = {
                        isRegisterMode = !isRegisterMode
                        errorMessage = null
                    },
                    isSecondary = true,
                    testTag = "auth_toggle_mode_button"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Guest Login
            GlassButton(
                text = "CONTINUE AS GUEST",
                onClick = {
                    val guestProfile = UserProfile(
                        username = "Guest Player",
                        avatarInitials = "GU",
                        classLevel = "10"
                    )
                    onAuthenticated(guestProfile)
                },
                isSecondary = true,
                testTag = "auth_guest_button"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
