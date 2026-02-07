package org.example.app.login

/**
 * Pure reducer functions for LoginState.
 *
 * This is intentionally implemented without Android dependencies to keep it deterministic and testable.
 */
object LoginReducer {

    /**
     * Apply a user intent to the current state to produce a new state.
     *
     * Note: Submit is handled as a two-phase operation:
     *  - [reduceSubmitStart] produces the loading state (or validation error)
     *  - if loading started, the ViewModel will later call [reduceSubmitResult]
     */
    fun reduce(current: LoginState, intent: LoginIntent): LoginState {
        return when (intent) {
            is LoginIntent.EmailChanged -> current.copy(
                email = intent.email,
                // Clear error/success on edits to keep UI responsive and predictable.
                error = null,
                success = false
            )

            is LoginIntent.PasswordChanged -> current.copy(
                password = intent.password,
                error = null,
                success = false
            )

            LoginIntent.Submit -> reduceSubmitStart(current)
        }
    }

    /**
     * Start a submit attempt:
     * - validates inputs
     * - if valid, enters loading state
     * - otherwise returns state with error
     */
    fun reduceSubmitStart(current: LoginState): LoginState {
        val email = current.email.trim()
        val password = current.password

        val validationError = validate(email, password)
        if (validationError != null) {
            return current.copy(
                isLoading = false,
                error = validationError,
                success = false
            )
        }

        return current.copy(
            isLoading = true,
            error = null,
            success = false
        )
    }

    /**
     * Apply the simulated auth result after loading.
     */
    fun reduceSubmitResult(current: LoginState): LoginState {
        val email = current.email.trim()
        val password = current.password

        val isAuthed = simulateAuth(email, password)
        return if (isAuthed) {
            current.copy(
                isLoading = false,
                error = null,
                success = true
            )
        } else {
            current.copy(
                isLoading = false,
                error = "Invalid credentials.",
                success = false
            )
        }
    }

    private fun validate(email: String, password: String): String? {
        if (email.isBlank()) return "Email is required."
        if (!isEmailLike(email)) return "Please enter a valid email."
        if (password.isBlank()) return "Password is required."
        return null
    }

    /**
     * Lightweight email check (not RFC compliant by design).
     */
    private fun isEmailLike(email: String): Boolean {
        // Simple pattern: something@something.something
        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        return emailRegex.matches(email)
    }

    /**
     * Simulated auth rule per requirements:
     * - accept if email endsWith("@example.com") AND password length>=6
     */
    private fun simulateAuth(email: String, password: String): Boolean {
        return email.lowercase().endsWith("@example.com") && password.length >= 6
    }
}
