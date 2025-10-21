package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

data class InspectionFormUiState(
    val dateMillis: Long? = null,
    val time: String = "",
    val make: String = "",
    val model: String = "",
    val year: String = "",
    val serviceType: String = "",
    val error: String? = null,
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false
)

class InspectionFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionFormUiState())
    val uiState: StateFlow<InspectionFormUiState> = _uiState.asStateFlow()

    fun updateDate(date: Long?) {
        _uiState.update { it.copy(dateMillis = date, error = null, isSubmitted = false) }
    }

    fun updateTime(time: String) {
        _uiState.update { it.copy(time = time, error = null, isSubmitted = false) }
    }

    fun updateMake(value: String) {
        _uiState.update { it.copy(make = value, error = null, isSubmitted = false) }
    }

    fun updateModel(value: String) {
        _uiState.update { it.copy(model = value, error = null, isSubmitted = false) }
    }

    fun updateYear(value: String) {
        _uiState.update { it.copy(year = value, error = null, isSubmitted = false) }
    }

    fun updateServiceType(value: String) {
        _uiState.update { it.copy(serviceType = value, error = null, isSubmitted = false) }
    }

    fun submitForm() {
        val currentState = _uiState.value
        val isValid = currentState.dateMillis != null &&
                currentState.time.isNotBlank() &&
                currentState.make.isNotBlank() &&
                currentState.model.isNotBlank() &&
                currentState.year.isNotBlank() &&
                currentState.serviceType.isNotBlank()

        if (!isValid) {
            _uiState.update { it.copy(error = "Todos los campos son obligatorios") }
            return
        }

        val userId = Firebase.auth.currentUser?.uid
        if (userId == null) {
            _uiState.update { it.copy(error = "Error: No se pudo identificar al usuario.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val inspectionData = hashMapOf(
            "userId" to userId,
            "dateMillis" to currentState.dateMillis,
            "time" to currentState.time,
            "make" to currentState.make,
            "model" to currentState.model,
            "year" to currentState.year,
            "serviceType" to currentState.serviceType,
            "status" to "Agendada", // Estado inicial
            "timestamp" to System.currentTimeMillis()
        )

        viewModelScope.launch {
            try {
                Firebase.firestore.collection("inspections")
                    .add(inspectionData)
                    .await()

                _uiState.update { it.copy(isLoading = false, isSubmitted = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun clearForm() {
        _uiState.value = InspectionFormUiState()
    }
}