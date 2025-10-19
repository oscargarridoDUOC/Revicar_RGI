package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Representa el estado de la UI para la pantalla del formulario.
 */
data class FormularioUiState(
    val fecha: Long? = null,
    val hora: String = "",
    val marca: String = "",
    val modelo: String = "",
    val anio: String = "",
    val servicio: String = "",
    val error: String? = null,
    val enviado: Boolean = false
)

/**
 * ViewModel que gestiona el estado (UiState) y la lógica del formulario.
 */
class FormularioViewModel : ViewModel() {

    // _uiState es privado y mutable (solo el ViewModel lo modifica).
    private val _uiState = MutableStateFlow(FormularioUiState())
    // uiState es público e inmutable (la UI solo lo lee).
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun actualizarFecha(fecha: Long?) {
        _uiState.update { it.copy(fecha = fecha, error = null) }
    }

    fun actualizarHora(hora: String) {
        _uiState.update { it.copy(hora = hora, error = null) }
    }

    fun actualizarMarca(valor: String) {
        _uiState.update { it.copy(marca = valor, error = null) }
    }

    fun actualizarModelo(valor: String) {
        _uiState.update { it.copy(modelo = valor, error = null) }
    }

    fun actualizarAnio(valor: String) {
        _uiState.update { it.copy(anio = valor, error = null) }
    }

    fun actualizarServicio(valor: String) {
        _uiState.update { it.copy(servicio = valor, error = null) }
    }

    fun confirmarFormulario() {
        val currentState = _uiState.value
        val esValido = currentState.fecha != null &&
                currentState.hora.isNotBlank() &&
                currentState.marca.isNotBlank() &&
                currentState.modelo.isNotBlank() &&
                currentState.anio.isNotBlank() &&
                currentState.servicio.isNotBlank()

        if (esValido) {
            _uiState.update { it.copy(enviado = true, error = null) }
            // Aquí podrías añadir lógica para enviar los datos a un servidor,
            // guardar en una base de datos, etc.
        } else {
            _uiState.update { it.copy(enviado = false, error = "Por favor, completa todos los campos") }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = FormularioUiState()
    }
}