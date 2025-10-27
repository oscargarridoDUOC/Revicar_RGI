package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BuyerReportUiState(
    val inspection: Inspection? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class BuyerReportViewModel(
    private val inspectionRepository: InspectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuyerReportUiState())
    val uiState: StateFlow<BuyerReportUiState> = _uiState.asStateFlow()

    fun loadInspectionReport(inspectionId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = inspectionRepository.getInspectionById(inspectionId)
            result.fold(
                onSuccess = { inspection ->
                    _uiState.update {
                        it.copy(isLoading = false, inspection = inspection)
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

    class Factory(
        private val repository: InspectionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BuyerReportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BuyerReportViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}