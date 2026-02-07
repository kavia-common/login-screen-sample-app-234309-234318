package org.example.app.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Compose-based login screen.
 *
 * This UI is driven by [LoginState] and emits user actions as [LoginIntent] to the existing MVI flow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Ocean Professional palette (from style guide).
    val background = Color(0xFFF9FAFB)
    val surface = Color(0xFFFFFFFF)
    val text = Color(0xFF111827)
    val primary = Color(0xFF2563EB)
    val secondary = Color(0xFFF59E0B)
    val error = Color(0xFFEF4444)
    val hint = Color(0xFF6B7280)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Sign in",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = text
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Email",
                    style = MaterialTheme.typography.bodyMedium.copy(color = text)
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading,
                    placeholder = { Text("you@example.com", color = hint) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primary,
                        unfocusedBorderColor = hint.copy(alpha = 0.35f),
                        cursorColor = primary,
                        focusedTextColor = text,
                        unfocusedTextColor = text,
                        disabledTextColor = text.copy(alpha = 0.6f)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Password",
                    style = MaterialTheme.typography.bodyMedium.copy(color = text)
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text("••••••", color = hint) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primary,
                        unfocusedBorderColor = hint.copy(alpha = 0.35f),
                        cursorColor = primary,
                        focusedTextColor = text,
                        unfocusedTextColor = text,
                        disabledTextColor = text.copy(alpha = 0.6f)
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { onIntent(LoginIntent.Submit) },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary,
                        disabledContainerColor = primary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.height(18.dp)
                        )
                        Text(
                            text = " Logging in…",
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Log in",
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val statusText: String
                val statusColor: Color
                when {
                    state.isLoading -> {
                        statusText = "Signing in…"
                        statusColor = hint
                    }

                    state.success -> {
                        statusText = "Success! You are logged in."
                        statusColor = secondary
                    }

                    state.error != null -> {
                        statusText = state.error
                        statusColor = error
                    }

                    else -> {
                        statusText = ""
                        statusColor = hint
                    }
                }

                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Demo login: email must end with @example.com and password length ≥ 6.",
                    color = hint,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Compose helper that adapts the existing lightweight [LoginViewModel] observer API
 * into Compose-friendly state, without introducing AndroidX ViewModel dependencies.
 */
// PUBLIC_INTERFACE
@Composable
fun rememberLoginState(viewModel: LoginViewModel): LoginState {
    /** Register an observer and expose the latest state to Compose; automatically removed on dispose. */
    var state by remember { mutableStateOf(LoginState()) }

    DisposableEffect(viewModel) {
        val observer: (LoginState) -> Unit = { newState -> state = newState }
        viewModel.observe(observer)
        onDispose {
            viewModel.removeObserver(observer)
        }
    }

    return state
}
