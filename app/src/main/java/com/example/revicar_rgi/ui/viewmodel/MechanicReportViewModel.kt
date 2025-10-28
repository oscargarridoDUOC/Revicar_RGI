package com.example.revicar_rgi.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.data.repository.LocalImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MechanicReportUiState(
    val inspection: Inspection? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val localImageUris: List<Uri> = emptyList()
)

class MechanicReportViewModel(
    private val inspectionRepository: InspectionRepository,
    private val localImageRepository: LocalImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MechanicReportUiState())
    val uiState: StateFlow<MechanicReportUiState> = _uiState.asStateFlow()

    fun loadInspectionReport(inspectionId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = inspectionRepository.getInspectionById(inspectionId)
            result.fold(
                onSuccess = { inspection ->
                    val imageUris = localImageRepository.getImageUrisForInspection(inspectionId)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            inspection = inspection,
                            localImageUris = imageUris
                        )
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
        private val repository: InspectionRepository,
        private val localImageRepository: LocalImageRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MechanicReportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MechanicReportViewModel(repository, localImageRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}