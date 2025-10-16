package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Representa los posibles estados de la pantalla de autenticación
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String, val isMechanic: Boolean) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Un flujo que nos dice si el usuario ya tiene una sesión iniciada
    val isLoggedIn = repository.getUidFlow().map { it != null }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Función que se llamará desde la pantalla de registro
    fun register(
        email: String,
        password: String,
        isMechanic: Boolean,
        name: String,
        lastName: String,
        run: String,
        phone: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.register(email, password, isMechanic, name, lastName, run, phone)

            _authState.value = result.fold(
                onSuccess = { uid -> AuthState.Success(uid, isMechanic) },
                onFailure = { error -> AuthState.Error(error.message ?: "Error desconocido en el registro") }
            )
        }
    }

    // Función que se llamará desde la pantalla de login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val loginResult = repository.login(email, password)

            loginResult.fold(
                onSuccess = { uid ->
                    // Si el login es exitoso, ahora consultamos el rol
                    val roleResult = repository.getUserRole(uid)
                    _authState.value = roleResult.fold(
                        onSuccess = { isMechanic -> AuthState.Success(uid, isMechanic) },
                        onFailure = { error -> AuthState.Error(error.message ?: "No se pudo verificar el rol") }
                    )
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Error de inicio de sesión")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}