package org.example.app.login

import android.os.Handler
import android.os.Looper

/**
 * Lightweight ViewModel-like state holder for the login screen.
 *
 * This project currently does not include AndroidX Lifecycle dependencies, so we avoid
 * androidx.lifecycle.ViewModel / LiveData here.
 *
 * TODO: In a future step, if we add AndroidX lifecycle-viewmodel and lifecycle-livedata,
 * we can convert this to a proper ViewModel + LiveData without changing the reducer/intents.
 */
class LoginViewModel {

    private val mainHandler = Handler(Looper.getMainLooper())

    private var currentState: LoginState = LoginState()

    private val observers = mutableSetOf<(LoginState) -> Unit>()

    // PUBLIC_INTERFACE
    fun observe(observer: (LoginState) -> Unit) {
        /** Register an observer that will be notified on every state update (immediately called with current state). */
        observers.add(observer)
        observer(currentState)
    }

    // PUBLIC_INTERFACE
    fun removeObserver(observer: (LoginState) -> Unit) {
        /** Remove a previously registered state observer. */
        observers.remove(observer)
    }

    // PUBLIC_INTERFACE
    fun dispatch(intent: LoginIntent) {
        /** Dispatch an MVI intent to update state and trigger side-effects (simulated auth). */
        val next = LoginReducer.reduce(currentState, intent)
        setState(next)

        // Side-effect simulation: if submit transitioned into loading, complete later.
        if (intent is LoginIntent.Submit && next.isLoading) {
            mainHandler.postDelayed(
                {
                    // Apply result based on whatever the latest state is at completion time.
                    val resultState = LoginReducer.reduceSubmitResult(currentState)
                    setState(resultState)
                },
                700L
            )
        }
    }

    private fun setState(newState: LoginState) {
        currentState = newState
        // Notify on main thread for UI safety
        if (Looper.myLooper() == Looper.getMainLooper()) {
            notifyObservers(newState)
        } else {
            mainHandler.post { notifyObservers(newState) }
        }
    }

    private fun notifyObservers(state: LoginState) {
        observers.forEach { it(state) }
    }
}
