package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.MechanicUiState
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.catch
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
            repository.getAvailableInspectionsFlow()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
                .collect { jobs ->
                    _uiState.update {
                        it.copy(isLoading = false, availableJobs = jobs, error = null)
                    }
                }
        }
    }

    fun loadMyJobs() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            repository.getMyJobsFlow()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
                .collect { jobs ->
                    _uiState.update {
                        it.copy(isLoading = false, myJobs = jobs, error = null)
                    }
                }
        }
    }

}