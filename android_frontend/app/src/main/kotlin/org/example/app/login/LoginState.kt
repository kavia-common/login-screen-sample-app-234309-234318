package org.example.app.login

/**
 * Immutable UI state for the login screen.
 */
data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val success: Boolean = false
)
