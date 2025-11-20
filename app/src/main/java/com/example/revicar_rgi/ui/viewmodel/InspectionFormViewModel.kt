package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.InspectionFormUiState
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.data.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InspectionFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionFormUiState())
    val uiState: StateFlow<InspectionFormUiState> = _uiState.asStateFlow()

    private val repository = InspectionRepository()
    private val vehicleRepository = VehicleRepository()

    init {
        loadMarcas()
    }

    fun updateDate(date: Long?) {
        _uiState.update { it.copy(dateMillis = date, error = null, isSubmitted = false) }
    }

    fun updateTime(time: String) {
        _uiState.update { it.copy(time = time, error = null, isSubmitted = false) }
    }

    fun updateMake(value: String) {
        _uiState.update { it.copy(make = value, error = null, isSubmitted = false) }
    }

    fun updateMakeFromMarca(marcaId: Int, marcaNombre: String) {
        _uiState.update { 
            it.copy(
                make = marcaNombre,
                selectedMarcaId = marcaId,
                model = "", // Limpiar modelo cuando se cambia la marca
                modelos = emptyList(),
                error = null,
                isSubmitted = false
            )
        }
        loadModelos(marcaId)
    }

    fun updateModel(value: String) {
        _uiState.update { it.copy(model = value, error = null, isSubmitted = false) }
    }

    fun updateModelFromModelo(modeloNombre: String) {
        _uiState.update { it.copy(model = modeloNombre, error = null, isSubmitted = false) }
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

        fun parsePrice(serviceString: String): Double {
            return serviceString.substringAfterLast("$")
                .replace(".", "")
                .toDoubleOrNull() ?: 0.0
        }

        val price = parsePrice(currentState.serviceType)

        if (price == 0.0) {
            _uiState.update { it.copy(error = "Error al procesar el precio del servicio") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = repository.submitInspection(
                dateMillis = currentState.dateMillis,
                time = currentState.time,
                make = currentState.make,
                model = currentState.model,
                year = currentState.year,
                comuna = currentState.comuna,
                direccion = currentState.direccion,
                serviceType = currentState.serviceType,
                servicePrice = price
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

    private fun loadMarcas() {
        _uiState.update { it.copy(isLoadingMarcas = true, error = null) }
        viewModelScope.launch {
            try {
                val marcas = vehicleRepository.getMarcas()
                _uiState.update { 
                    it.copy(
                        marcas = marcas,
                        isLoadingMarcas = false
                    )
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Error de conexión: No se puede conectar al servidor. Verifica tu conexión a internet."
                    e.message?.contains("timeout") == true -> 
                        "Error de conexión: El servidor tardó demasiado en responder."
                    e.message?.contains("404") == true -> 
                        "Error: Endpoint no encontrado. Verifica la URL de la API."
                    e.message?.contains("500") == true -> 
                        "Error del servidor: Problema interno del servidor."
                    else -> "Error al cargar las marcas: ${e.message ?: e.localizedMessage ?: "Error desconocido"}"
                }
                _uiState.update { 
                    it.copy(
                        isLoadingMarcas = false,
                        error = errorMessage
                    )
                }
            }
        }
    }

    private fun loadModelos(marcaId: Int) {
        _uiState.update { it.copy(isLoadingModelos = true, error = null) }
        viewModelScope.launch {
            try {
                val modelos = vehicleRepository.getModelosByMarca(marcaId)
                _uiState.update { 
                    it.copy(
                        modelos = modelos,
                        isLoadingModelos = false
                    )
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Error de conexión: No se puede conectar al servidor. Verifica tu conexión a internet."
                    e.message?.contains("timeout") == true -> 
                        "Error de conexión: El servidor tardó demasiado en responder."
                    e.message?.contains("404") == true -> 
                        "Error: No se encontraron modelos para esta marca."
                    e.message?.contains("500") == true -> 
                        "Error del servidor: Problema interno del servidor."
                    else -> "Error al cargar los modelos: ${e.message ?: e.localizedMessage ?: "Error desconocido"}"
                }
                _uiState.update { 
                    it.copy(
                        isLoadingModelos = false,
                        error = errorMessage
                    )
                }
            }
        }
    }
}