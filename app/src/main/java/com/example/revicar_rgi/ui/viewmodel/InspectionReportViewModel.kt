package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InspectionReportUiState(
    val reportText: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

class InspectionReportViewModel(
    private val repository: InspectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionReportUiState())
    val uiState: StateFlow<InspectionReportUiState> = _uiState.asStateFlow()

    fun onReportTextChange(newText: String) {
        _uiState.update { it.copy(reportText = newText) }
    }

    fun saveReport(inspectionId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val saveResult = repository.completeInspection(
                inspectionId = inspectionId,
                reportText = _uiState.value.reportText
            )

            saveResult.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isSaving = false, error = error.message) }
                }
            )
        }
    }

    class Factory(
        private val repository: InspectionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InspectionReportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InspectionReportViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}