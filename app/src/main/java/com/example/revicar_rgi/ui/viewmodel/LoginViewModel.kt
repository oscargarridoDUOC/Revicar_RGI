package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onEmailChange(newValue: String) {
        _email.value = newValue
        validateForm()
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
        validateForm()
    }
    private fun validateForm() {
        val currentErrorMessage = when {
            _email.value.isBlank() -> "El email es obligatorio"
            !_email.value.contains("@") -> "El formato del email es inválido"
            _password.value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            _password.value.length > 12 -> "La contraseña debe tener como máximo 12 caracteres"
            else -> null
        }
        _errorMessage.value = currentErrorMessage
        _isFormValid.value = currentErrorMessage == null
    }
}