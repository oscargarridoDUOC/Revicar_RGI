package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.MechanicUiState
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MechanicViewModel(
    private val repository: InspectionRepository = InspectionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MechanicUiState())
    val uiState: StateFlow<MechanicUiState> = _uiState.asStateFlow()

    init {
        loadAllJobs()
    }

    fun loadAllJobs() {
        loadAvailableJobs()
        loadMyJobs()
    }

    fun loadAvailableJobs() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = repository.getAvailableInspections()
            result.fold(
                onSuccess = { jobs ->
                    _uiState.update {
                        it.copy(isLoading = false, availableJobs = jobs)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
            )
        }
    }

    fun loadMyJobs() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = repository.getMyJobs()
            result.fold(
                onSuccess = { jobs ->
                    _uiState.update {
                        it.copy(isLoading = false, myJobs = jobs)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
            )
        }
    }

    fun acceptJob(inspectionId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = repository.acceptInspection(inspectionId)
            result.fold(
                onSuccess = {
                    loadAllJobs()
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
            )
        }
    }

    fun completeJob(inspectionId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = repository.completeInspection(inspectionId)
            result.fold(
                onSuccess = {
                    loadMyJobs()
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
            )
        }
    }
}