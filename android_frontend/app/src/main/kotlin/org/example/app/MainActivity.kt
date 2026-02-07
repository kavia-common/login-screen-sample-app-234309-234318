package org.example.app

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.example.app.login.LoginIntent
import org.example.app.login.LoginState
import org.example.app.login.LoginViewModel

class MainActivity : Activity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var statusText: TextView

    private val viewModel: LoginViewModel = LoginViewModel()

    private val stateObserver: (LoginState) -> Unit = { state ->
        render(state)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        statusText = findViewById(R.id.statusText)

        setupIntents()
        observeState()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeObserver(stateObserver)
    }

    private fun setupIntents() {
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // no-op
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.dispatch(LoginIntent.EmailChanged(s?.toString() ?: ""))
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // no-op
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.dispatch(LoginIntent.PasswordChanged(s?.toString() ?: ""))
            }
        })

        loginButton.setOnClickListener {
            viewModel.dispatch(LoginIntent.Submit)
        }
    }

    private fun observeState() {
        viewModel.observe(stateObserver)
    }

    private fun render(state: LoginState) {
        val inputsEnabled = !state.isLoading
        emailEditText.isEnabled = inputsEnabled
        passwordEditText.isEnabled = inputsEnabled
        loginButton.isEnabled = inputsEnabled

        // Keep button text informative during loading
        loginButton.text = if (state.isLoading) "Logging in…" else "Log in"

        when {
            state.isLoading -> {
                statusText.visibility = View.VISIBLE
                statusText.text = "Signing in…"
                statusText.setTextColor(0xFF6B7280.toInt()) // gray-500
            }

            state.success -> {
                statusText.visibility = View.VISIBLE
                statusText.text = "Success! You are logged in."
                statusText.setTextColor(0xFF2563EB.toInt()) // primary blue
            }

            state.error != null -> {
                statusText.visibility = View.VISIBLE
                statusText.text = state.error
                statusText.setTextColor(0xFFEF4444.toInt()) // error red
            }

            else -> {
                // Neutral idle state: show a subtle hint area empty.
                statusText.visibility = View.VISIBLE
                statusText.text = ""
                statusText.setTextColor(0xFF6B7280.toInt())
            }
        }
    }
}
