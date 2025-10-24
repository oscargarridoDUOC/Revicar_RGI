package com.example.revicar_rgi.ui.screens.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.revicar_rgi.ui.components.PhoneNumberInput
import com.example.revicar_rgi.ui.viewmodel.AuthState
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import com.example.revicar_rgi.utils.ValidationUtils


@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onSuccessNavigation: (isMechanic: Boolean) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var run by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isMechanic by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                onSuccessNavigation(state.isMechanic)
                authViewModel.resetAuthState()
            }
            is AuthState.Error -> {
                validationError = state.message
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { newName ->
                if (newName.all { it.isLetter() || it.isWhitespace() } && newName.length <= 100) {
                    name = newName
                }
            },
            label = { Text("Nombre *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = authState != AuthState.Loading
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = { newLastName ->
                if (newLastName.all { it.isLetter() || it.isWhitespace() } && newLastName.length <= 100 ){
                    lastName = newLastName
                }
            },
            label = { Text("Apellidos *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = authState != AuthState.Loading
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = run,
            onValueChange = { newRun ->
                if (newRun.length <= 10) {
                    if (newRun.all { it.isDigit() || it.equals('k', ignoreCase = true) || it == '-' }) {
                        run = newRun
                    }
                }
            },
            label = { Text("Run * (ej: 12345678-k)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = authState != AuthState.Loading
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = authState != AuthState.Loading
        )
        Spacer(Modifier.height(8.dp))
        PhoneNumberInput(
            phone = phone,
            onPhoneChange = { newPhoneValue ->
                if (newPhoneValue.all { it.isDigit() } && newPhoneValue.length <= 9) {
                    phone = newPhoneValue
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña *") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = authState != AuthState.Loading
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña *") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = authState != AuthState.Loading
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isMechanic,
                onCheckedChange = { isMechanic = it },
                enabled = authState != AuthState.Loading
            )
            Text("¿Eres mecánico?", modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(Modifier.height(16.dp))

        if (validationError != null) {
            Text(
                text = validationError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                validationError = null
                val fullPhoneNumber = "+56${phone}"
                val cleanRun = run.trim()

                val isRunFormatValid = cleanRun.length >= 3 &&
                        cleanRun.length <= 10 &&
                        cleanRun[cleanRun.length - 2] == '-'

                if (name.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || cleanRun.isBlank()) {
                    validationError = "Todos los campos con * son obligatorios."
                } else if (!isRunFormatValid) {
                    validationError = "El formato del RUN no es válido (ej: 12345678-k)."
                } else if (!ValidationUtils.isValidEmail(email.trim())) {
                    validationError = "El formato del email no es válido."
                } else if (password.length < 6 || password.length > 12) {
                    validationError = "La contraseña debe tener entre 6 y 12 caracteres."
                } else if (phone.length != 9 || !phone.startsWith('9')) {
                    validationError = "El teléfono debe tener 9 dígitos y empezar con 9."
                } else if (password != confirmPassword) {
                    validationError = "Las contraseñas no coinciden."
                } else {
                    authViewModel.register(email.trim(), password.trim(), isMechanic, name.trim(), lastName.trim(), cleanRun, fullPhoneNumber.trim())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState != AuthState.Loading
        ) {
            if (authState == AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Registrarse")
            }
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}