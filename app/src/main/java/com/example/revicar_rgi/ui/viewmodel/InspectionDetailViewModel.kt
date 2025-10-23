package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.InspectionDetailUiState
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InspectionDetailViewModel(
    private val inspectionRepository: InspectionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionDetailUiState())
    val uiState: StateFlow<InspectionDetailUiState> = _uiState.asStateFlow()

    fun loadDetails(inspectionId: String) {
        _uiState.update { it.copy(isLoading = true, error = null, actionSuccess = false) }
        viewModelScope.launch {
            val inspectionResult = inspectionRepository.getInspectionById(inspectionId)

            inspectionResult.fold(
                onSuccess = { inspection ->
                    _uiState.update { it.copy(inspection = inspection) }

                    if (inspection.mechanicId != null) {
                        loadMechanicDetails(inspection.mechanicId)
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    private fun loadMechanicDetails(mechanicId: String) {
        viewModelScope.launch {
            val mechanicResult = authRepository.getUserById(mechanicId)
            mechanicResult.fold(
                onSuccess = { mechanic ->
                    _uiState.update { it.copy(isLoading = false, mechanic = mechanic) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    fun acceptJob(inspectionId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = inspectionRepository.acceptInspection(inspectionId)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(actionSuccess = true) }
                    loadDetails(inspectionId)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    fun completeJob(inspectionId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = inspectionRepository.completeInspection(inspectionId)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(actionSuccess = true) }
                    loadDetails(inspectionId)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    class Factory(
        private val inspectionRepository: InspectionRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InspectionDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InspectionDetailViewModel(inspectionRepository, authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}