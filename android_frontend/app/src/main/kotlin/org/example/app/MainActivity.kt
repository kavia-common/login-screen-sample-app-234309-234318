package org.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import org.example.app.login.LoginIntent
import org.example.app.login.LoginScreen
import org.example.app.login.LoginViewModel
import org.example.app.login.rememberLoginState

/**
 * Single-activity entry point that hosts the Compose-based login UI.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = rememberLoginState(viewModel)

            MaterialTheme {
                Surface {
                    LoginScreen(
                        state = state,
                        onIntent = { intent: LoginIntent -> viewModel.dispatch(intent) }
                    )
                }
            }
        }
    }
}
