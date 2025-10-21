package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.InspectionFormUiState
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InspectionFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionFormUiState())
    val uiState: StateFlow<InspectionFormUiState> = _uiState.asStateFlow()

    private val repository = InspectionRepository()

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

    fun updateComuna(value: String) {
        _uiState.update { it.copy(comuna = value, error = null, isSubmitted = false) }
    }

    fun updateDireccion(value: String) {
        _uiState.update { it.copy(direccion = value, error = null, isSubmitted = false) }
    }

    fun submitForm() {
        val currentState = _uiState.value

        val isValid = currentState.dateMillis != null &&
                currentState.time.isNotBlank() &&
                currentState.make.isNotBlank() &&
                currentState.model.isNotBlank() &&
                currentState.year.isNotBlank() &&
                currentState.comuna.isNotBlank() &&
                currentState.direccion.isNotBlank() &&
                currentState.serviceType.isNotBlank()

        if (!isValid) {
            _uiState.update { it.copy(error = "Todos los campos son obligatorios") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = repository.submitInspection(
                dateMillis = currentState.dateMillis!!,
                time = currentState.time,
                make = currentState.make,
                model = currentState.model,
                year = currentState.year,
                comuna = currentState.comuna,
                direccion = currentState.direccion,
                serviceType = currentState.serviceType
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    fun clearForm() {
        _uiState.value = InspectionFormUiState()
    }
}