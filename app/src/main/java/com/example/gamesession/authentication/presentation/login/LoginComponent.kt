package com.example.gamesession.authentication.presentation.login

import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface LoginComponent {
    val model: StateFlow<Model>

    fun onLoginChanged(value: String)
    fun onPasswordChanged(value: String)
    fun onSubmit()

    @Serializable
    data class Model(
        val login: String = "",
        val password: String = "",
        val loginFailed: Boolean = false
    ) {
        val isLoginError: Boolean
            get() = login.any {
                val block = Character.UnicodeBlock.of(it)
                block != Character.UnicodeBlock.CYRILLIC
            }

        val isPasswordError: Boolean
            get() = password.isNotEmpty() && (
                    password.length < 6 ||
                            !password.any { it.isDigit() } ||
                            !password.any { it.isLetter() && it.code < 128 }
                    )

        val isButtonEnabled: Boolean
            get() = login.isNotEmpty() && password.isNotEmpty() && !isLoginError && !isPasswordError
    }
}