package org.example.app.login

/**
 * MVI intents emitted by the View (Activity) and processed by the ViewModel.
 */
sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent

    /**
     * User pressed the login button.
     */
    data object Submit : LoginIntent
}
