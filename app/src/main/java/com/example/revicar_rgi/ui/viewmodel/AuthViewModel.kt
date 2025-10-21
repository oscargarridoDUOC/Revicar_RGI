package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String, val isMechanic: Boolean) : AuthState()
    data class Error(val message: String) : AuthState()
}


enum class SplashCheckState {
    Loading,
    NavigateToLogin,
    NavigateToUserHome,
    NavigateToMechanicHome
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _splashState = MutableStateFlow<SplashCheckState>(SplashCheckState.Loading)
    val splashState: StateFlow<SplashCheckState> = _splashState.asStateFlow()

    fun checkCurrentUser() {
        viewModelScope.launch {
            _splashState.value = SplashCheckState.Loading

            val uid = repository.getUidFlow().first()

            if (uid == null) {
                _splashState.value = SplashCheckState.NavigateToLogin
            } else {
                val roleResult = repository.getUserRole(uid)
                _splashState.value = roleResult.fold(
                    onSuccess = { isMechanic ->
                        if (isMechanic) {
                            SplashCheckState.NavigateToMechanicHome
                        } else {
                            SplashCheckState.NavigateToUserHome
                        }
                    },
                    onFailure = {
                        repository.logout()
                        SplashCheckState.NavigateToLogin
                    }
                )
            }
        }
    }


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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val loginResult = repository.login(email, password)

            loginResult.fold(
                onSuccess = { uid ->
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

    fun resetAuthState() {
        _authState.value = AuthState.Idle
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